package com.example.todo.dto;
import com.example.todo.domain.Todo;
import com.example.todo.dto.TodoResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TodoResponseTest {
 @Test
    void shouldReturnGets(){
     TodoRequest req = new TodoRequest();
     req.setTitle("Title");
     req.setDescription("Description");
     req.setDone(false);

     Todo domain = req.toDomain();

     TodoResponse response = TodoResponse.from(domain);

     assertEquals(domain.getId(), response.getId(),"Will be transfered to DTO (response)");
     assertEquals(domain.getTitle(), response.getTitle(),"Will be transfered to DTO (response)");
     assertEquals(domain.getDescription(), response.getDescription(),"Will be transfered to DTO (response)");
     assertEquals(domain.getCreatedAt(), response.getCreatedAt(),"Will be transfered to DTO (response)");
     assertEquals(domain.getUpdatedAt(), response.getUpdatedAt(),"Will be transfered to DTO (response)");
     assertEquals(domain.isDone(), response.isDone(),"Will be transfered to DTO (response)");

 }
}
