package com.vidal.task.Service;

import com.vidal.task.Model.GenerateRequest;
import com.vidal.task.Model.GenerateResponse;
import com.vidal.task.Model.FinalQueryRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WebhookService {

    @Autowired
    private RestTemplate restTemplate;

    public void executeTask() {

        try {

            String generateUrl =
                    "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";

            GenerateRequest request =
                    new GenerateRequest("Gowtham", "REG12347", "gowthamgowtham7067@email.com");

            GenerateResponse response =
                    restTemplate.postForObject(generateUrl,
                            request,
                            GenerateResponse.class);

            String accessToken = response.getAccessToken();


            String finalSqlQuery = getQuestion1Query();
            // String finalSqlQuery = getQuestion2Query();

            FinalQueryRequest finalRequest =
                    new FinalQueryRequest(finalSqlQuery);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", accessToken);

            HttpEntity<FinalQueryRequest> entity =
                    new HttpEntity<>(finalRequest, headers);

            ResponseEntity<String> finalResponse =
                    restTemplate.postForEntity(
                            "https://bfhldevapigw.healthrx.co.in/hiring/testWebhook/JAVA",
                            entity,
                            String.class
                    );

            System.out.println("Submission Response: "
                    + finalResponse.getBody());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getQuestion1Query() {

        return "SELECT department_name, salary, employee_name, age FROM (" +
                " SELECT d.department_name," +
                " SUM(p.amount) AS salary," +
                " CONCAT(e.first_name, ' ', e.last_name) AS employee_name," +
                " TIMESTAMPDIFF(YEAR, e.dob, CURDATE()) AS age," +
                " DENSE_RANK() OVER (PARTITION BY d.department_id ORDER BY SUM(p.amount) DESC) AS rnk" +
                " FROM employee e" +
                " JOIN department d ON e.department = d.department_id" +
                " JOIN payments p ON e.emp_id = p.emp_id" +
                " WHERE DAY(p.payment_time) <> 1" +
                " GROUP BY d.department_id, d.department_name, e.emp_id, e.first_name, e.last_name, e.dob" +
                ") t WHERE rnk = 1";
    }

    private String getQuestion2Query() {

        return "SELECT d.department_name," +
                " AVG(TIMESTAMPDIFF(YEAR, e.dob, CURDATE())) AS average_age," +
                " GROUP_CONCAT(CONCAT(e.first_name, ' ', e.last_name) ORDER BY e.emp_id SEPARATOR ', ') AS employee_list" +
                " FROM employee e" +
                " JOIN department d ON e.department = d.department_id" +
                " JOIN payments p ON e.emp_id = p.emp_id" +
                " WHERE p.amount > 70000" +
                " GROUP BY d.department_id, d.department_name" +
                " ORDER BY d.department_id DESC";
    }
}
