
package com.example.data;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.example.data.repository.PersonaRepository;

@SpringBootTest
class SpringDataApplicationTests {
@Autowired
private PersonaRepository usuarioRepository;
}