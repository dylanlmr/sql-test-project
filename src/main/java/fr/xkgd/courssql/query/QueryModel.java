package fr.xkgd.courssql.query;

import java.util.ArrayList;

public class QueryModel {

    private String query;
    private ArrayList<String> columns;
    private ArrayList<String> rows;

    public QueryModel() {
        query = "";
        columns = new ArrayList<>();
        rows = new ArrayList<>();
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public ArrayList<String> getColumns() {
        return columns;
    }

    public void setColumns(ArrayList<String> columns) {
        this.columns = columns;
    }

    public void addColumn(String column) {
        columns.add(column);
    }

    public void removeColumn(String column) {
        columns.remove(column);
    }

    public void removeColumn(int index) {
        columns.remove(index);
    }

    public void clearColumns() {
        columns.clear();
    }

    public ArrayList<String> getRows() {
        return rows;
    }

    public void setRows(ArrayList<String> rows) {
        this.rows = rows;
    }

    public void addRow(String row) {
        rows.add(row);
    }

    public void removeRow(String row) {
        rows.remove(row);
    }

    public void removeRow(int index) {
        rows.remove(index);
    }

    public void clearRows() {
        rows.clear();
    }
}
