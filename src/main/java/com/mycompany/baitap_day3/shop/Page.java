package com.mycompany.baitap_day3.shop;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/** Shared HTML layout/CSS for the shop pages. */
final class Page {

    private Page() {
    }

    static PrintWriter begin(HttpServletResponse resp, String title) throws IOException {
        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        out.println("<!DOCTYPE html><html><head><meta charset=\"UTF-8\">");
        out.println("<title>Murach's Java Servlets and JSP</title><style>");
        out.println("body{font-family:Arial,Helvetica,sans-serif;font-size:14px;background:#f4f6f8;margin:0}");
        out.println("main{width:700px;margin:0 auto;padding-top:20px}");
        out.println("h1{color:#008080;font-size:20px;margin:0 0 10px}");
        out.println("table{border-collapse:collapse;width:100%;background:#fff}");
        out.println("th,td{border:1px solid #cfd8dc;padding:7px;text-align:left}");
        out.println("th{background:#e8f4f3}");
        out.println("td.r,th.r{text-align:right}");
        out.println("form{margin:0;display:inline}");
        out.println("input[type=submit]{background:#008080;color:#fff;border:0;padding:6px 12px;font-weight:bold;cursor:pointer}");
        out.println("input[type=text]{width:40px}");
        out.println("</style></head><body><main>");
        out.println("<h1>" + title + "</h1>");
        return out;
    }

    static void end(PrintWriter out) {
        out.println("</main></body></html>");
    }

    static String money(double v) {
        return String.format("$%.2f", v);
    }

    static String esc(String s) {
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }
}
