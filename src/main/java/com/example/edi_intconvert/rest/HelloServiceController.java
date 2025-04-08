package com.example.edi_intconvert.rest;

import ru.edi.convert_api.HelloService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/hello")
public class HelloServiceController implements HelloService {

    @Override
    @PostMapping("/send")
    public String sendHello(@RequestBody String message) {
        return "Привет от intConvert, " + message.toUpperCase();
    }

    @Override
    public String transferToManufacturing(Integer integer) {
        return integer.toString();
    }
}
