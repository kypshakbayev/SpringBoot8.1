package crm.system.springTask.__1.controllers;

import crm.system.springTask.__1.db.ApplicationRequest;
import crm.system.springTask.__1.db.Courses;
import crm.system.springTask.__1.db.Operators;
import crm.system.springTask.__1.repositories.OperatorsRepository;
import crm.system.springTask.__1.repositories.ReqRepository;
import crm.system.springTask.__1.repositories.CoursesRepository;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Builder
@Controller
public class CRMController {

    @Autowired
    private ReqRepository reqRepository;

    @Autowired
    private CoursesRepository coursesRepository;

    @Autowired
    private OperatorsRepository operatorsRepository;

    @GetMapping(value = "/")
    public String index(Model model) {
        model.addAttribute("requests", reqRepository.findAll());
        return "index";
    }

    @GetMapping(value = "/add")
    public String addRequest(Model model) {
        model.addAttribute("courses", coursesRepository.findAll());
        return "add";
    }

    @PostMapping("/addRequest")
    public String addRequest(@RequestParam(name = "fio") String userName,
                             @RequestParam(name = "kursy") Long courseId,
                             @RequestParam(name = "comment") String commentary,
                             @RequestParam(name = "phone") String phone) {

        Courses selectedCourse = coursesRepository.findById(courseId).orElse(null);

        ApplicationRequest appReq = ApplicationRequest.builder()
                .userName(userName)
                .commentary(commentary)
                .phone(phone)
                .handled(false)
                .courses(selectedCourse)
                .build();
        reqRepository.save(appReq);
        return "redirect:/";
    }

    @GetMapping(value = "/details/{id}")
    public String detailsRequest(@PathVariable("id") Long id, Model model) {

        ApplicationRequest appReq = reqRepository.findById(id).orElse(null);
        if (appReq.isHandled() == false) {
            model.addAttribute("newRequest", "*** Новая необработанная заявка ***");
        } else {
            model.addAttribute("processedRequest", "Обработанная заявка");
        }
        model.addAttribute("request", appReq);

        List<Operators> operators = operatorsRepository.findAll();
        model.addAttribute("operators", operators);

        List<Operators> appReqOper = appReq.getOperators();

        model.addAttribute("appReqOper", appReqOper);

        return "details";

    }

    @PostMapping("/deleteRequest")
    public String deleteRequest(@RequestParam("id") Long id) {
        reqRepository.deleteById(id);
        return "redirect:/";
    }

    @GetMapping("/filter")
    public String filter(@RequestParam boolean handled, Model model) {
        model.addAttribute("requests", reqRepository.findByHandled(handled));
        return "index";
    }

    @PostMapping("/addOperator")
    public String addOperator(@RequestParam(name = "requestId") Long requestId,
                              @RequestParam(name = "operators", required = false) List<Long> operatorIds) {
        ApplicationRequest appReq = reqRepository.findById(requestId).orElse(null);

        if (appReq != null) {
            if (operatorIds != null && !operatorIds.isEmpty()) {
                List<Operators> operators = operatorsRepository.findAllById(operatorIds);
                appReq.setOperators(operators);
            }
            appReq.setHandled(true);
            reqRepository.save(appReq);
        }

        return "redirect:/details/" + requestId; // Перенаправляем на страницу деталей запроса
    }

    @PostMapping(value = "/deleteOper")
    public String deleteOper(@RequestParam(name = "id") Long requestId,
                             @RequestParam(name = "selectOper") List<Long> selectOper) {
        List<Operators> operators = new ArrayList<>();
        for (Long id : selectOper) {
            operators.add(operatorsRepository.findById(id).orElse(null));
        }
        ApplicationRequest appReq = reqRepository.findById(requestId).orElse(null);
        if (appReq != null) {
            if (operators != null && !operators.isEmpty()) {
                appReq.getOperators().removeAll(operators);
                reqRepository.save(appReq);
            }
        }

        return "redirect:/details/" + requestId;
    }
}
