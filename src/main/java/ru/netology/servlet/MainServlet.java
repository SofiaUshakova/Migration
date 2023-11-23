package ru.netology.servlet;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.netology.JavaCofig.JavaConfig;
import ru.netology.controller.PostController;
import ru.netology.repository.PostRepository;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;

public class MainServlet extends HttpServlet {
  private PostController controller;
  private final String PATH = "/api/posts";
  private final String DIGIT = "/\\d+";

  @Override
  public void init() {
//    final var repository = new PostRepository();
//    final var service = new PostService(repository);
//    controller = new PostController(service);
    final var context = new AnnotationConfigApplicationContext(JavaConfig.class);
    controller = context.getBean(PostController.class);
  }

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp) {
    // если деплоились в root context, то достаточно этого
    try {
      final var path = req.getRequestURI();
      final var method = req.getMethod();
      // primitive routing
      if (method.equals("GET") && path.equals(PATH)) {
        controller.all(resp);
        return;
      }
      if (method.equals("GET") && path.matches(PATH + DIGIT)) {
        // easy way
        final var id = findID(path);
        controller.getById(id, resp);
        return;
      }
      if (method.equals("POST") && path.equals(PATH)) {
        controller.save(req.getReader(), resp);
        return;
      }
      if (method.equals("DELETE") && path.matches(PATH + DIGIT)) {
        // easy way
        final var id = findID(path);
        controller.removeById(id, resp);
        return;
      }
      resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
    } catch (Exception e) {
      e.printStackTrace();
      resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }
  private long findID(String path){
    return Long.parseLong(path.substring(path.lastIndexOf("/")));
  }
}

