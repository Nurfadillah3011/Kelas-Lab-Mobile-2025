package com.example.t6mobile_h071231080;

import java.util.List;
//character response: sebagai pembungkus untuk data yg diterima dari API agar retrofit bisa secara otomatis mengonversi json jadi objek java
public class CharacterResponse {
    private Info info;
    private List<Character> results;


    public static class Info {
        private int count;
        private int pages;
        private String next;
        private String prev;

        // Getter dan Setter
        public int getCount() { return count; }
        public void setCount(int count) { this.count = count; }
        public int getPages() { return pages; }
        public void setPages(int pages) { this.pages = pages; }
        public String getNext() { return next; }
        public void setNext(String next) { this.next = next; }
        public String getPrev() { return prev; }
        public void setPrev(String prev) { this.prev = prev; }
    }


    public Info getInfo() { return info; }
    public void setInfo(Info info) { this.info = info; }
    public List<Character> getResults() { return results; }
    public void setResults(List<Character> results) { this.results = results; }
}