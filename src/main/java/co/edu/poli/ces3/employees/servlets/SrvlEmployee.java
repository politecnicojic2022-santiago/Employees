package co.edu.poli.ces3.employees.servlets;

import co.edu.poli.ces3.employees.entities.Employee;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

@WebServlet(name = "SrvlEmployee", value = "/SrvlEmployee")
public class SrvlEmployee extends HttpServlet {

    public static  ArrayList<Employee> EMPLOYEES = new ArrayList<>(Arrays.asList(
            new Employee("1111", "Carlos", "Perez", 80),
            new Employee("9999", "Ana", "Diaz", 80),
            new Employee("888888", "Diego", "Trujillo", 20),
            new Employee("1828392", "Diana", "Perez", 30)
    ));
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletOutputStream out = response.getOutputStream();
        response.setContentType("application/json");
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        String id = request.getParameter("id");
        if (id != null){
            Employee emp = buscarEmpleado(id);
            out.print(gson.toJson(emp));
        }else {
            out.print(gson.toJson(SrvlEmployee.EMPLOYEES));
        }
        out.flush();
    }

    private Employee buscarEmpleado(String id) {
        for (Employee emp: SrvlEmployee.EMPLOYEES) {
            if (emp.getId().equals(id)){
                return emp;
            }
        }
        return null;
    }

    private String getParamsFromPost(HttpServletRequest request) throws IOException {
        BufferedReader reader = request.getReader();
        StringBuilder sb = new StringBuilder();
        String line = reader.readLine();
        while (line != null) {
            sb.append(line + "\n");
            line = reader.readLine();
        }
        reader.close();
        String params = sb.toString();

        return params;
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletOutputStream out = response.getOutputStream();
        response.setContentType("application/json");
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        JsonObject body = new JsonParser().parse(this.getParamsFromPost(request)).getAsJsonObject();
        Random rd = new Random();
        int min = 0;
        int max = 1000;
        Employee employee = new Employee(
                String.valueOf(rd.nextInt(max - min) + min),
                body.get("name").getAsString(),
                body.get("lastName").getAsString(),
                body.get("age").getAsInt()
        );

        SrvlEmployee.EMPLOYEES.add(employee);

        out.print(gson.toJson(employee));
        out.flush();
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletOutputStream out = response.getOutputStream();
        response.setContentType("application/json");
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        String id = request.getParameter("id");
        JsonObject body = new JsonParser().parse(this.getParamsFromPost(request)).getAsJsonObject();
        if (id != null) {
            Employee emp = buscarEmpleado(id);
            out.print(gson.toJson(emp));
            emp.setName(body.get("name").getAsString());
            emp.setLastName(body.get("lastName").getAsString());
            emp.setAge(body.get("age").getAsInt());
        }
        out.print(gson.toJson(SrvlEmployee.EMPLOYEES));
    }
}
