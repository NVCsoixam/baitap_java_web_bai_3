package com.mycompany.baitap_day3.shop;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;

@WebServlet(name = "CartServlet", urlPatterns = {"/cart"})
public class CartServlet extends HttpServlet {

    @SuppressWarnings("unchecked")
    private Map<String, Integer> cart(HttpServletRequest req) {
        HttpSession s = req.getSession();
        Map<String, Integer> c = (Map<String, Integer>) s.getAttribute("cart");
        if (c == null) {
            c = new LinkedHashMap<>();
            s.setAttribute("cart", c);
        }
        return c;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        showCart(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String action = req.getParameter("action");
        String code = req.getParameter("code");
        Map<String, Integer> cart = cart(req);
        if (action == null) {
            action = "";
        }
        switch (action) {
            case "add":
                if (Product.find(code) != null) {
                    cart.merge(code, 1, Integer::sum);
                }
                break;
            case "update":
                int q = 0;
                try {
                    q = Integer.parseInt(req.getParameter("quantity").trim());
                } catch (RuntimeException e) {
                    // ignore invalid number -> treated as remove
                }
                if (q > 0) {
                    cart.put(code, q);
                } else {
                    cart.remove(code);
                }
                break;
            case "remove":
                cart.remove(code);
                break;
            case "shop":
                resp.sendRedirect(req.getContextPath() + "/");
                return;
            case "checkout":
                showCheckout(req, resp);
                return;
            default:
                break;
        }
        showCart(req, resp);
    }

    private void showCart(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Map<String, Integer> cart = cart(req);
        PrintWriter out = Page.begin(resp, "Your cart");
        if (cart.isEmpty()) {
            out.println("<p>Your cart is empty.</p>");
        } else {
            out.println("<table><tr><th>Quantity</th><th>Description</th><th class=\"r\">Price</th>"
                    + "<th class=\"r\">Amount</th><th></th></tr>");
            for (Map.Entry<String, Integer> e : cart.entrySet()) {
                Product p = Product.find(e.getKey());
                int qty = e.getValue();
                out.println("<tr><td><form action=\"cart\" method=\"post\">"
                        + "<input type=\"hidden\" name=\"action\" value=\"update\">"
                        + "<input type=\"hidden\" name=\"code\" value=\"" + p.getCode() + "\">"
                        + "<input type=\"text\" name=\"quantity\" value=\"" + qty + "\"> "
                        + "<input type=\"submit\" value=\"Update\"></form></td>"
                        + "<td>" + Page.esc(p.getDescription()) + "</td>"
                        + "<td class=\"r\">" + Page.money(p.getPrice()) + "</td>"
                        + "<td class=\"r\">" + Page.money(p.getPrice() * qty) + "</td>"
                        + "<td><form action=\"cart\" method=\"post\">"
                        + "<input type=\"hidden\" name=\"action\" value=\"remove\">"
                        + "<input type=\"hidden\" name=\"code\" value=\"" + p.getCode() + "\">"
                        + "<input type=\"submit\" value=\"Remove Item\"></form></td></tr>");
            }
            out.println("</table>");
            out.println("<p><b>To change the quantity</b>, enter the new quantity and click Update.</p>");
        }
        out.println("<br><form action=\"cart\" method=\"post\">"
                + "<input type=\"hidden\" name=\"action\" value=\"shop\">"
                + "<input type=\"submit\" value=\"Continue Shopping\"></form> ");
        if (!cart.isEmpty()) {
            out.println("<form action=\"cart\" method=\"post\">"
                    + "<input type=\"hidden\" name=\"action\" value=\"checkout\">"
                    + "<input type=\"submit\" value=\"Checkout\"></form>");
        }
        Page.end(out);
    }

    private void showCheckout(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Map<String, Integer> cart = cart(req);
        double total = 0;
        PrintWriter out = Page.begin(resp, "Checkout");
        out.println("<table><tr><th>Description</th><th>Quantity</th><th>Price</th></tr>");
        for (Map.Entry<String, Integer> e : cart.entrySet()) {
            Product p = Product.find(e.getKey());
            total += p.getPrice() * e.getValue();
            out.println("<tr><td>" + Page.esc(p.getDescription()) + "</td><td>" + e.getValue()
                    + "</td><td>" + Page.money(p.getPrice()) + "</td></tr>");
        }
        out.println("<tr><td colspan=\"2\"><b>Total</b></td><td><b>" + Page.money(total) + "</b></td></tr>");
        out.println("</table><br><form action=\"cart\" method=\"post\">"
                + "<input type=\"hidden\" name=\"action\" value=\"shop\">"
                + "<input type=\"submit\" value=\"Continue Shopping\"></form>");
        Page.end(out);
    }
}
