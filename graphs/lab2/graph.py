import json
import math
from pathlib import Path

import matplotlib.pyplot as plt

PROJECT_ROOT = Path.cwd()
RESULTS_FILE = PROJECT_ROOT / "graphs" / "lab2" / "jmh-results.json"
OUTPUT_DIR = PROJECT_ROOT / "graphs" / "lab2"

def load_results(path: Path):
    with path.open("r", encoding="utf-8") as f:
        return json.load(f)


def normalize_row(row):
    benchmark = row["benchmark"]
    params = row.get("params", {})
    metric = row["primaryMetric"]
    return {
        "benchmark": benchmark,
        "class_name": benchmark.split(".")[-2],
        "method_name": benchmark.split(".")[-1],
        "mode": row["mode"],
        "size": int(params.get("size", 0)),
        "threshold": float(params.get("threshold", 0.0)),
        "query_type": params.get("queryType"),
        "score": metric["score"],
        "error": metric.get("scoreError", 0.0),
        "unit": metric["scoreUnit"],
    }


def group_build(rows):
    grouped = {}
    for row in rows:
        if row["method_name"] != "bspBuild":
            continue
        key = (row["method_name"], row["mode"])
        grouped.setdefault(key, []).append(row)
    return grouped


def group_find(rows):
    grouped = {}
    for row in rows:
        if row["method_name"] != "bspFindWithThreshold":
            continue
        key = (row["method_name"], row["mode"], row["query_type"])
        grouped.setdefault(key, []).append(row)
    return grouped


def plot_build(method_name, mode, rows):
    plt.figure(figsize=(10, 6))

    aggregated = {}
    for row in rows:
        aggregated.setdefault(row["size"], []).append(row)

    unit = rows[0]["unit"]
    x = []
    y = []
    err = []
    for size in sorted(aggregated):
        size_rows = aggregated[size]
        scores = [r["score"] for r in size_rows]
        mean_score = sum(scores) / len(scores)
        if len(scores) > 1:
            variance = sum((score - mean_score) ** 2 for score in scores) / len(scores)
            error = math.sqrt(variance)
        else:
            error = size_rows[0]["error"]
        x.append(size)
        y.append(mean_score)
        err.append(error)

    plt.errorbar(x, y, yerr=err, marker="o", capsize=4, label=method_name)

    plt.title(f"{method_name} ({mode})")
    plt.xlabel("Input size")
    plt.ylabel(unit)
    plt.grid(True, alpha=0.3)
    plt.legend()
    plt.tight_layout()

    OUTPUT_DIR.mkdir(parents=True, exist_ok=True)
    output_file = OUTPUT_DIR / f"{method_name}_{mode}.png"
    plt.savefig(output_file, dpi=160)
    plt.close()


def plot_find(method_name, mode, query_type, rows):
    plt.figure(figsize=(10, 6))

    by_threshold = {}
    for row in rows:
        by_threshold.setdefault(row["threshold"], []).append(row)

    unit = rows[0]["unit"]

    for threshold, threshold_rows in sorted(by_threshold.items(), key=lambda item: item[0]):
        threshold_rows = sorted(threshold_rows, key=lambda x: x["size"])
        x = [r["size"] for r in threshold_rows]
        y = [r["score"] for r in threshold_rows]
        err = [r["error"] for r in threshold_rows]

        plt.errorbar(x, y, yerr=err, marker="o", capsize=4, label=f"threshold={threshold:g}")

    plt.title(f"{method_name} ({query_type}, {mode})")
    plt.xlabel("Input size")
    plt.ylabel(unit)
    plt.grid(True, alpha=0.3)
    plt.legend()
    plt.tight_layout()

    OUTPUT_DIR.mkdir(parents=True, exist_ok=True)
    output_file = OUTPUT_DIR / f"{method_name}_{query_type}_{mode}.png"
    plt.savefig(output_file, dpi=160)
    plt.close()


def main():
    if not RESULTS_FILE.exists():
        raise FileNotFoundError(f"Results file not found: {RESULTS_FILE}")

    rows = [normalize_row(row) for row in load_results(RESULTS_FILE)]

    build_groups = group_build(rows)
    for (method_name, mode), group_rows in build_groups.items():
        plot_build(method_name, mode, group_rows)

    find_groups = group_find(rows)
    for (method_name, mode, query_type), group_rows in find_groups.items():
        plot_find(method_name, mode, query_type, group_rows)

    print(f"Saved charts to: {OUTPUT_DIR}")


if __name__ == "__main__":
    main()
