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
//@RequestMapping("/action")
//public class ActionController {
//
//    @Autowired
//    private ActionService actionService;
//
//    @GetMapping("/list")
//    public OldBaseResponse listActions(@RequestParam(value = "pageNo", defaultValue = "1")Integer pageNo,
//                                       @RequestParam(value = "pageSize", defaultValue = "10")Integer pageSize,
//                                       @RequestParam(required = false) String word,
//                                       @RequestParam(required = false, defaultValue = "0") Long tagId) {
//        LoginVo loginVo = AuthContext.getUser();
//        ActionQuery actionQuery = ActionQuery.builder()
//                .pageNo(pageNo)
//                .pageSize(pageSize)
//                .word(word)
//                .tagId(tagId).build();
//        ListActionResponse listActionResponse = actionService.queryActions(actionQuery, loginVo);
//        return BaseResponse.success(listActionResponse);
//    }
//
////    @GetMapping("/get")
////    public BaseResponse getAction(@RequestParam Long id) {
////        ActionVo actionVo = actionService.getActionVo(id);
////        return BaseResponse.success(actionVo);
////    }
////
////    @PostMapping("/save")
////    public BaseResponse saveAction(@RequestBody SaveActionRequest request) {
////        LoginVo user = AuthContext.getUser();
////        actionService.saveAction(request, user);
////        return BaseResponse.success();
////    }
////
////    @PostMapping("/update")
////    public BaseResponse updateAction(@RequestBody UpdateActionRequest request) {
////        LoginVo user = AuthContext.getUser();
////        actionService.updateAction(request, user);
////        return BaseResponse.success();
////    }
////
////    @GetMapping("/attribute")
////    public BaseResponse getActionAttribute() {
////        return BaseResponse.success(actionService.getActionAttribute());
////    }
//}
