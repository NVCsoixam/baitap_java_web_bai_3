package com.mycompany.baitap_day3.shop;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "CatalogServlet", urlPatterns = {"", "/index"})
public class CatalogServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter out = Page.begin(resp, "CD list");
        out.println("<table><tr><th>Description</th><th class=\"r\">Price</th><th></th></tr>");
        for (Product p : Product.CATALOG) {
            out.println("<tr><td>" + Page.esc(p.getDescription()) + "</td><td class=\"r\">"
                    + Page.money(p.getPrice()) + "</td><td>"
                    + "<form action=\"cart\" method=\"post\">"
                    + "<input type=\"hidden\" name=\"action\" value=\"add\">"
                    + "<input type=\"hidden\" name=\"code\" value=\"" + p.getCode() + "\">"
                    + "<input type=\"submit\" value=\"Add To Cart\"></form></td></tr>");
        }
        out.println("</table>");
        Page.end(out);
    }
}
