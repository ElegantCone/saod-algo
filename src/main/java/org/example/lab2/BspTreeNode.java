package org.example.lab2;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.lab2.figure.Figure;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class BspTreeNode {
    Line splitter;
    BspTreeNode front;
    BspTreeNode back;
    List<Figure> coplanarFigures;
    List<Figure> figures;
}