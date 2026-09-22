package graphcollection.exception;

import lombok.Getter;

@Getter
public enum GraphParseError {
    EMPTY_VERTEX_ID("Не задан идентификатор для вершины!"),
    EMPTY_VERTEX_NAME("Не задано имя для вершины!"),
    INVALID_VERTEX_NAME_FORMAT("Имя вершины должно содержать только цифры и символы, максимум 10"),
    VERTEX_IDS_NOT_UNIQUE("Идентификаторы вершин должны быть уникальны!"),
    VERTEX_NAMES_NOT_UNIQUE("Имена вершин должны быть уникальны!"),
    EMPTY_EDGE_VERTEX("Не заданы вершины для ребра!"),
    INVALID_EDGE_VERTEX("Ребро (%s, %s) содержит не существующую вершину %s!");

    private final String description;

    GraphParseError(String description) {
        this.description = description;
    }

    public String getFormattedDescription(Object... args) {
        return String.format(description, args);
    }
}
