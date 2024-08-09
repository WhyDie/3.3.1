package ua.whydie;

public class Employee {
    String name;
    int salary;
    IT pos;
    int height;
    Employee left, right;

    public Employee(String name, int salary, IT pos) {
        this.name = name;
        this.salary = salary;
        this.pos = pos;
        this.height = 1;
    }

    @Override
    public String toString() {
        return "Employee{" + "name='" + name + '\'' + ", salary=" + salary + ", pos=" + pos + '}';
    }
}
