//package com.fushuhealth.recovery.device.controller;
//
//import com.fushuhealth.recovery.common.api.BaseResponse;
//import com.fushuhealth.recovery.common.api.OldBaseResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/patient")
//public class PatientController {
//
//    @Autowired
//    private UserService userService;
//
//    @GetMapping("/list")
//    public OldBaseResponse listPatient(@RequestParam(value = "pageNo", defaultValue = "1")Integer pageNo,
//                                       @RequestParam(value = "pageSize", defaultValue = "10")Integer pageSize,
//                                       @RequestParam(value = "name", required = false)String name,
//                                       @RequestParam(value = "trainingStatus", required = false)Byte trainingStatus) {
//
//        LoginVo user = AuthContext.getUser();
//        PatientListResponse response = userService.listUser(pageNo, pageSize, name, user);
//        return OldBaseResponse.success(response);
//    }
//
////    @GetMapping("/get")
////    public BaseResponse getPatient(Long id) {
////        UserDetailVo user = userService.findUser(id);
////        return BaseResponse.success(user);
////    }
////
////    @PostMapping("/save")
////    public BaseResponse savePatient(@RequestBody SavePatientRequest request) {
////        LoginVo user = AuthContext.getUser();
////        userService.saveUser(user, request);
////        return BaseResponse.success();
////    }
////
////    @PostMapping("/update")
////    public BaseResponse updatePatient(@RequestBody UpdateUserRequest request) {
////        userService.updateUser(request);
////        return BaseResponse.success();
////    }
////
////    @PostMapping("/updateArchives")
////    public BaseResponse updatePatient(@RequestBody UpdateUserArchivesRequest request) {
////        userService.updateUserArchives(request);
////        return BaseResponse.success();
////    }
//}
