package com.example.calculator.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import java.util.Map;


@Controller
public class CalcController {


@RequestMapping("/")
public String index() {
return "index"; // Thymeleaf template src/main/resources/templates/index.html
}


@PostMapping(path = "/api/calc", consumes = "application/json", produces = "application/json")
@ResponseBody
public ResponseEntity<?> calculate(@RequestBody Map<String, String> payload) {
try {
double a = Double.parseDouble(payload.getOrDefault("a", "0"));
double b = Double.parseDouble(payload.getOrDefault("b", "0"));
String op = payload.getOrDefault("op", "+");
double result;
switch (op) {
case "+": result = a + b; break;
case "-": result = a - b; break;
case "*": result = a * b; break;
case "/": result = b == 0 ? Double.NaN : a / b; break;
case "%": result = a % b; break;
default: result = Double.NaN; break;
}
return ResponseEntity.ok(Map.of("result", result));
} catch (Exception ex) {
return ResponseEntity.badRequest().body(Map.of("error", "invalid input"));
}
}
}