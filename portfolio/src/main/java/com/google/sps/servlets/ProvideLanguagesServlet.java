package com.google.sps.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.cloud.translate.Language;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.gson.Gson;

/** Servlet that provides a list of languages supported by the translate API */
@WebServlet("/provided-languages")
public class ProvideLanguagesServlet extends HttpServlet {
    
  private static final long serialVersionUID = -930957438693188860L;

  @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Translate translate = TranslateOptions.newBuilder().setProjectId("internplayground").
            setQuotaProjectId("internplayground").build().getService();

        List<Language> languages = translate.listSupportedLanguages();

        Gson gson = new Gson();
        String jsonLanguages = gson.toJson(languages);
        response.setContentType("application/json; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().println(jsonLanguages);
    }
}