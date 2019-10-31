package com.codegym.controller;

import com.codegym.model.Department;
import com.codegym.model.Employee;
import com.codegym.model.EmployeeForm;
import com.codegym.service.DepartmentService;
import com.codegym.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

@PropertySource("classpath:global_config_app.properties")
@Controller
public class EmployeeController {
    @Autowired
    Environment env;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private DepartmentService departmentService;
    @ModelAttribute("departments")
    public Iterable<Department> departments(){
        return departmentService.findAll();
    }
    @GetMapping("/create-employee")
    public ModelAndView showCreateForm(){
        ModelAndView modelAndView = new ModelAndView("/employee/create");
        modelAndView.addObject("employee", new Employee());
        return modelAndView;
    }
//    @PostMapping("/create-employee")
//    public ModelAndView saveEmployee(@ModelAttribute("employee") Employee employee){
//        employeeService.save(employee);
//        ModelAndView modelAndView = new ModelAndView("/employee/create");
//        modelAndView.addObject("employee", new Employee());
//        modelAndView.addObject("message", "New employee added successfully");
//        return modelAndView;
//    }

    @PostMapping("/create-employee")
    public ModelAndView saveProduct( @ModelAttribute EmployeeForm employeeForm, BindingResult result) {

        // thong bao neu xay ra loi
        if (result.hasErrors()) {
            System.out.println("Result Error Occured" + result.getAllErrors());
        }

        // lay ten file
        MultipartFile multipartFile = employeeForm.getAvatar();
        String fileName = multipartFile.getOriginalFilename();
        String fileUpload = env.getProperty("file_upload").toString();

        // luu file len server
        try {
            //multipartFile.transferTo(imageFile);
            FileCopyUtils.copy(employeeForm.getAvatar().getBytes(), new File(fileUpload + fileName));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        // tham khảo: https://github.com/codegym-vn/spring-static-resources

        // tao doi tuong de luu vao db
        Employee employeeObject  = new Employee(employeeForm.getName(),employeeForm.getBirthDate(), employeeForm.getAddress(), employeeForm.getGender()
        ,fileName , employeeForm.getSalary(), employeeForm.getDepartment());

        // luu vao db
        //productService.save(productObject);
        employeeService.save(employeeObject);

        ModelAndView modelAndView = new ModelAndView("/employee/create");
        modelAndView.addObject("employee", new EmployeeForm());
        modelAndView.addObject("message","successes!");
        return modelAndView;

    }
    @GetMapping("/employees")
    public ModelAndView listEmployee(@RequestParam("s")Optional<String> s, @PageableDefault(value = 5) Pageable pageable){
        Page<Employee> employees;
        if (s.isPresent()){
            employees = employeeService.findAllByNameContaining(s.get(), pageable);
        }else {
            employees = employeeService.findAll(pageable);
        }
        ModelAndView modelAndView = new ModelAndView("/employee/list");
        modelAndView.addObject("employees", employees);
        return modelAndView;
    }
    @GetMapping("/edit-employee/{id}")
    public ModelAndView showEditForm(@PathVariable Long id){
        Employee employee = employeeService.findById(id);
        if (employee!=null){
            ModelAndView modelAndView = new ModelAndView("/employee/edit");
            modelAndView.addObject("employee", employee);
            return modelAndView;
        }else {
            ModelAndView modelAndView = new ModelAndView("/error.404");
            return modelAndView;
        }
    }
    @PostMapping("/edit-employee")
    public ModelAndView updateEmployee(@ModelAttribute("employee") Employee employee){
        employeeService.save(employee);
        ModelAndView modelAndView = new ModelAndView("/employee/edit");
        modelAndView.addObject("employee", employee);
        modelAndView.addObject("message", "Employee updated successfully");
        return modelAndView;
    }
    @GetMapping("/delete-employee/{id}")
    public ModelAndView showDeleteForm(@PathVariable Long id){
        Employee employee = employeeService.findById(id);
        if (employee!=null){
            ModelAndView modelAndView = new ModelAndView("/employee/delete");
            modelAndView.addObject("employee", employee);
            return modelAndView;
        }else {
            ModelAndView modelAndView = new ModelAndView("/error.404");
            return modelAndView;
        }
    }
    @PostMapping("/delete-employee")
    public String deleteEmployee(@ModelAttribute("employee") Employee employee){
        employeeService.remove(employee.getId());
        return "redirect:employees";
    }
}
