package ua.whydie;
import java.util.ArrayList;
import java.util.List;

public class Organization {
    private Employee root;

    public int height(Employee N) {
        if (N == null)
            return 0;
        return N.height;
    }

    public int max(int a, int b) {
        return (a > b) ? a : b;
    }

    private Employee rightRotate(Employee y) {
        Employee x = y.left;
        Employee T2 = x.right;

        x.right = y;
        y.left = T2;

        y.height = max(height(y.left), height(y.right)) + 1;
        x.height = max(height(x.left), height(x.right)) + 1;

        return x;
    }

    private Employee leftRotate(Employee x) {
        Employee y = x.right;
        Employee T2 = y.left;

        y.left = x;
        x.right = T2;

        x.height = max(height(x.left), height(x.right)) + 1;
        y.height = max(height(y.left), height(y.right)) + 1;

        return y;
    }

    private int getBalance(Employee N) {
        if (N == null)
            return 0;
        return height(N.left) - height(N.right);
    }

    private Employee insert(Employee node, Employee employee) {
        if (node == null)
            return employee;

        if (employee.salary < node.salary)
            node.left = insert(node.left, employee);
        else if (employee.salary > node.salary)
            node.right = insert(node.right, employee);
        else
            return node;

        node.height = 1 + max(height(node.left), height(node.right));

        int balance = getBalance(node);

        if (balance > 1 && employee.salary < node.left.salary)
            return rightRotate(node);

        if (balance < -1 && employee.salary > node.right.salary)
            return leftRotate(node);

        if (balance > 1 && employee.salary > node.left.salary) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        if (balance < -1 && employee.salary < node.right.salary) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
    }

    public void addEmployee(String name, int salary, IT pos) {
        Employee employee = new Employee(name, salary, pos);
        root = insert(root, employee);
    }

    private void preOrder(Employee node) {
        if (node != null) {
            System.out.print(node.name + " ");
            preOrder(node.left);
            preOrder(node.right);
        }
    }

    private void inOrder(Employee node) {
        if (node != null) {
            inOrder(node.left);
            System.out.print(node.name + " ");
            inOrder(node.right);
        }
    }

    private Employee find(Employee node, String name) {
        if (node == null || node.name.equals(name))
            return node;

        if (name.compareTo(node.name) < 0)
            return find(node.left, name);

        return find(node.right, name);
    }

    public List<Employee> getEmployeesWithSalaryAbove(int salary) {
        List<Employee> result = new ArrayList<>();
        getEmployeesWithSalaryAbove(root, salary, result);
        return result;
    }

    private void getEmployeesWithSalaryAbove(Employee node, int salary, List<Employee> result) {
        if (node != null) {
            if (node.salary > salary)
                result.add(node);
            getEmployeesWithSalaryAbove(node.left, salary, result);
            getEmployeesWithSalaryAbove(node.right, salary, result);
        }
    }

    public boolean checkEmployeeExists(String name) {
        return find(root, name) != null;
    }

    private Employee minValueNode(Employee node) {
        Employee current = node;

        while (current.left != null)
            current = current.left;

        return current;
    }

    private Employee deleteNode(Employee root, String name) {
        if (root == null)
            return root;

        if (name.compareTo(root.name) < 0)
            root.left = deleteNode(root.left, name);
        else if (name.compareTo(root.name) > 0)
            root.right = deleteNode(root.right, name);
        else {
            if ((root.left == null) || (root.right == null)) {
                Employee temp = null;
                if (temp == root.left)
                    temp = root.right;
                else
                    temp = root.left;

                if (temp == null) {
                    temp = root;
                    root = null;
                } else
                    root = temp;
            } else {
                Employee temp = minValueNode(root.right);
                root.name = temp.name;
                root.salary = temp.salary;
                root.pos = temp.pos;
                root.right = deleteNode(root.right, temp.name);
            }
        }

        if (root == null)
            return root;

        root.height = max(height(root.left), height(root.right)) + 1;

        int balance = getBalance(root);

        if (balance > 1 && getBalance(root.left) >= 0)
            return rightRotate(root);

        if (balance > 1 && getBalance(root.left) < 0) {
            root.left = leftRotate(root.left);
            return rightRotate(root);
        }

        if (balance < -1 && getBalance(root.right) <= 0)
            return leftRotate(root);

        if (balance < -1 && getBalance(root.right) > 0) {
            root.right = rightRotate(root.right);
            return leftRotate(root);
        }

        return root;
    }

    public void removeEmployee(String name) {
        root = deleteNode(root, name);
    }

    public List<Employee> getEmployeesByLevel(int maxLevel) {
        List<Employee> result = new ArrayList<>();
        bfsLevels(root, 1, maxLevel, result);
        return result;
    }

    private void bfsLevels(Employee node, int level, int maxLevel, List<Employee> result) {
        if (node == null || level > maxLevel)
            return;

        if (result.size() < level)
            result.add(node);
        else
            result.set(level - 1, node);

        bfsLevels(node.left, level + 1, maxLevel, result);
        bfsLevels(node.right, level + 1, maxLevel, result);
    }

    public Employee getSibling(String name) {
        return findSibling(root, name);
    }

    private Employee findSibling(Employee node, String name) {
        if (node == null)
            return null;

        if (node.left != null && node.left.name.equals(name))
            return node.right;
        if (node.right != null && node.right.name.equals(name))
            return node.left;

        Employee left = findSibling(node.left, name);
        if (left != null)
            return left;

        return findSibling(node.right, name);
    }

    public List<Employee> getEmployeesInSalaryOrder() {
        List<Employee> result = new ArrayList<>();
        inOrderSalary(root, result);
        return result;
    }

    private void inOrderSalary(Employee node, List<Employee> result) {
        if (node != null) {
            inOrderSalary(node.left, result);
            result.add(node);
            inOrderSalary(node.right, result);
        }
    }

    public static void main(String[] args) {
        Organization org = new Organization();
        org.addEmployee("Alice", 50000, IT.FULLSTACK_DEV);
        org.addEmployee("Bob", 60000, IT.FRONTEND_DEV);
        org.addEmployee("Charlie", 55000, IT.BACKEND_DEV);

        System.out.println("Pre-order traversal of the AVL tree:");
        org.preOrder(org.root);
        System.out.println("\nIn-order traversal of the AVL tree:");
        org.inOrder(org.root);

        // Check if an employee exists
        System.out.println("\nDoes Bob exist in the organization? " + org.checkEmployeeExists("Bob"));

        // Get employees with salary above a certain value
        System.out.println("\nEmployees with salary above 55000:");
        List<Employee> employees = org.getEmployeesWithSalaryAbove(55000);
        for (Employee emp : employees) {
            System.out.println(emp);
        }

        // Remove an employee
        org.removeEmployee("Charlie");

        // Get employees in BFS order up to level 3
        System.out.println("\nEmployees from level 1 to 3:");
        List<Employee> employeesByLevel = org.getEmployeesByLevel(3);
        for (Employee emp : employeesByLevel) {
            System.out.println(emp);
        }

        // Get sibling of an employee
        System.out.println("\nSibling of Alice: " + org.getSibling("Alice"));

        // Get employees in order of salary
        System.out.println("\nEmployees sorted by salary:");
        List<Employee> employeesSortedBySalary = org.getEmployeesInSalaryOrder();
        for (Employee emp : employeesSortedBySalary) {
            System.out.println(emp);
        }
    }
}
