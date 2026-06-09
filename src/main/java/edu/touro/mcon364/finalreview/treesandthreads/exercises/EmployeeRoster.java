package edu.touro.mcon364.finalreview.treesandthreads.exercises;

import edu.touro.mcon364.finalreview.treesandthreads.model.Employee;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.*;

/**
 * In-class Exercise 2 — Employee Roster (TreeMap + TreeSet + Streams)
 * <p>
 * Scenario: a company has employees spread across several departments.
 * You need to organize them so that departments are listed alphabetically
 * and employees within each department are also sorted alphabetically by name.
 * <p>
 * This exercise practises:
 * - TreeMap<String, TreeSet<Employee>> as a two-level sorted structure.
 * - Collectors.groupingBy with a TreeSet downstream collector.
 * - Stream flatMap to merge employees from all departments into one list.
 * - NavigableMap range queries (departments A-M vs N-Z).
 * <p>
 * Before coding, think about:
 * - Why do we use TreeSet inside the map rather than ArrayList?
 * - What comparator drives the ordering inside each TreeSet?
 * - If two employees have the same name and department but different salaries,
 * are they considered the same element inside the TreeSet?
 * <p>
 * Requirements:
 * - The constructor receives the list of employees.
 * - buildRoster() returns a TreeMap<String, TreeSet<Employee>> grouping
 * employees by department. Both the departments and the employees within
 * each department must be in sorted order.
 * - getTopEarnerPerDepartment() returns a Map<String, Employee> with the
 * highest-paid employee in each department.
 * - getAllEmployeesSorted() returns all employees across all departments in
 * a single sorted list (alphabetical by name).
 * - getDepartmentsInRange(from, to) returns a NavigableMap slice containing
 * only the departments whose names fall in [from, to] inclusive.
 * <p>
 * Do not use explicit loops. Use streams and collectors.
 */
public class EmployeeRoster {

    private final List<Employee> employees;

    public EmployeeRoster(List<Employee> employees) {
        // TODO: validate non-null, store a defensive copy
        /*
         * if (employees == null) {
         * throw new IllegalArgumentException("employees list cannot be null");
         * }
         * this.employees = List.copyOf(employees);
         */
        this.employees = List.copyOf(Objects.requireNonNull(employees, "employees list cannot be null"));
    }

    /**
     * Groups employees by department into a sorted two-level structure.
     *
     * @return sorted map: department name -> sorted set of employees
     */
    public TreeMap<String, TreeSet<Employee>> buildRoster() {
        // TODO
        return employees.stream()
                .collect(Collectors.groupingBy(
                        Employee::department,
                        TreeMap::new,
                        Collectors.toCollection(TreeSet::new)));
    }

    /**
     * Returns the highest-paid employee in each department.
     *
     * @return map of department name -> top earner
     */
    public Map<String, Employee> getTopEarnerPerDepartment() {
        // TODO
        return employees.stream()
                .collect(Collectors.toMap(Employee::department, Function.identity(),
                        BinaryOperator.maxBy(Comparator.comparingInt(Employee::salary))));
    }

    /**
     * Returns every employee across all departments in a single alphabetical list.
     *
     * @return globally sorted employee list
     */
    public List<Employee> getAllEmployeesSorted() {
        // TODO
        return buildRoster().values().stream()
                .flatMap(Collection::stream)
                .toList();
    }

    /**
     * Returns a view of the roster containing only departments in [from, to].
     *
     * @param from lower bound department name (inclusive)
     * @param to   upper bound department name (inclusive)
     * @return navigable sub-map
     */
    public NavigableMap<String, TreeSet<Employee>> getDepartmentsInRange(String from, String to) {
        // TODO
        return buildRoster().subMap(from, true, to, true);
    }
}

