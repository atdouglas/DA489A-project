module se.myhappyplants {
    requires org.postgresql.jdbc;
    requires jbcrypt;
    requires io.javalin;
    requires com.google.gson;
    requires java.sql;
    requires org.jetbrains.annotations;
    requires io.github.cdimascio.dotenv.java;
    requires java.net.http;

    exports se.myhappyplants.shared to com.google.gson;
    opens se.myhappyplants.shared to com.google.gson;
    opens se.myhappyplants.server.repositories to com.google.gson;

    exports se.myhappyplants.server.repositories;
    exports se.myhappyplants.server.addplantsutility;
    exports se.myhappyplants.server;
}