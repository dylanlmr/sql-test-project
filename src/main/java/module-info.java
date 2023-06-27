module fr.xkgd.courssql {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.java;


    opens fr.xkgd.courssql to javafx.fxml;
    opens fr.xkgd.courssql.query to javafx.fxml;
    opens fr.xkgd.courssql.home to javafx.fxml;

    exports fr.xkgd.courssql;
    exports fr.xkgd.courssql.connection;
}