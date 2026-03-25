import json
from pathlib import Path

import matplotlib.pyplot as plt

PROJECT_ROOT = Path.cwd()
RESULTS_FILE = PROJECT_ROOT / "target" / "jmh-results.json"
OUTPUT_DIR = PROJECT_ROOT / "target" / "jmh-charts"

def load_results(path: Path):
    with path.open("r", encoding="utf-8") as f:
        return json.load(f)


def short_name(benchmark: str) -> str:
    parts = benchmark.split(".")
    if len(parts) < 2:
        return benchmark
    return f"{parts[-2]}.{parts[-1]}"


def group_by_operation(rows):
    grouped = {}
    for row in rows:
        benchmark = row["benchmark"]
        mode = row["mode"]
        params = row.get("params", {})
        size = int(params.get("size", 0))
        metric = row["primaryMetric"]

        class_name = benchmark.split(".")[-2]
        method_name = benchmark.split(".")[-1]
        key = (method_name, mode)

        grouped.setdefault(key, []).append({
            "class_name": class_name,
            "benchmark": benchmark,
            "size": size,
            "score": metric["score"],
            "error": metric.get("scoreError", 0.0),
            "unit": metric["scoreUnit"],
        })
    return grouped


def plot_group(method_name, mode, rows):
    plt.figure(figsize=(10, 6))

    by_class = {}
    for row in rows:
        by_class.setdefault(row["class_name"], []).append(row)

    unit = rows[0]["unit"]

    for class_name, class_rows in sorted(by_class.items()):
        class_rows = sorted(class_rows, key=lambda x: x["size"])
        x = [r["size"] for r in class_rows]
        y = [r["score"] for r in class_rows]
        err = [r["error"] for r in class_rows]

        plt.errorbar(x, y, yerr=err, marker="o", capsize=4, label=class_name)

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


def plot_individual(rows):
    for row in rows:
        benchmark = row["benchmark"]
        mode = row["mode"]
        size = int(row.get("params", {}).get("size", 0))
        metric = row["primaryMetric"]

        plt.figure(figsize=(6, 4))
        plt.bar([short_name(benchmark)], [metric["score"]], yerr=[metric.get("scoreError", 0.0)], capsize=6)
        plt.title(f"{short_name(benchmark)} | size={size} | {mode}")
        plt.ylabel(metric["scoreUnit"])
        plt.grid(True, axis="y", alpha=0.3)
        plt.tight_layout()

        OUTPUT_DIR.mkdir(parents=True, exist_ok=True)
        safe_name = benchmark.replace(".", "_")
        output_file = OUTPUT_DIR / f"{safe_name}_size{size}_{mode}.png"
        plt.savefig(output_file, dpi=160)
        plt.close()


def main():
    if not RESULTS_FILE.exists():
        raise FileNotFoundError(f"Results file not found: {RESULTS_FILE}")

    rows = load_results(RESULTS_FILE)

    grouped = group_by_operation(rows)
    for (method_name, mode), group_rows in grouped.items():
        plot_group(method_name, mode, group_rows)

    plot_individual(rows)

    print(f"Saved charts to: {OUTPUT_DIR}")


if __name__ == "__main__":
    main()
