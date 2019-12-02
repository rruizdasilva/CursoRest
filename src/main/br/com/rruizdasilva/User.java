package br.com.rruizdasilva;

public class User {

    private String name;
    private Integer age;
    private Double salary;

    public User(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }

    public Double getSalary() {
        return salary;
    }

}
