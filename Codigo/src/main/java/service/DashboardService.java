package service;

import java.io.File;
import java.util.Scanner;
import spark.Request;
import spark.Response;

public class DashboardService {

    public String getDashboard(Request req, Response res) {
        res.type("text/html; charset=UTF-8");
        String form = "";
        try {
            Scanner entrada = new Scanner(new File("src/main/resources/public/dashboard_form.html"));
            while (entrada.hasNext()) {
                form += (entrada.nextLine() + "\n");
            }
            entrada.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return form;
    }
}
