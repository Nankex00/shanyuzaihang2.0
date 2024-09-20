package com.fushuhealth.recovery.device.security;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class AuthContext {

    private static Object getRequetHeader(String headerName) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes) {
            HttpServletRequest request = ((ServletRequestAttributes)requestAttributes).getRequest();
            Object object = request.getAttribute(headerName);
            return object;
        }
        return null;
    }

//    public static Integer getUserId() {
//        return getUser().getId();
//    }

//    public static void validateAuthenticatedUser(Integer userId) {
//        Integer currentUserId = getUserId();
//        if (currentUserId != userId) {
//            throw new PermissionDeniedException(ResultCode.PERMISSION_ERROR);
//        }
//    }

//    public static boolean hasRoleAdmin() {
//        UserDetailVo user = getUser();
//        List<RoleVo> roles = user.getRoles();
//        if (CollectionUtils.isEmpty(roles)) {
//            return false;
//        }
//        List<String> roleNameList = roles.stream().map(roleVo -> roleVo.getRoleName()).collect(Collectors.toList());
//        return roleNameList.contains(RoleEnum.ROLE_ADMIN.getName());
//    }

//    private static UserDetailVo getUser() {
//        UserDetailVo userDetail = (UserDetailVo) getRequetHeader(com.fushuhealth.klarity.security.AuthConstant.CURRENT_USER_HEADER);
//        if (userDetail == null) {
//            throw new NotLoginException(ResultCode.USER_NOT_LOGIN);
//        }
//        return userDetail;
//    }
}
