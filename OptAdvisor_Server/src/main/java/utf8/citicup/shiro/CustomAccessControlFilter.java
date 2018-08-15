package utf8.citicup.shiro;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;
import utf8.citicup.service.util.JsonParse;
import utf8.citicup.service.util.StatusMsg;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

class CustomAccessControlFilter extends AccessControlFilter {

    private Logger logger = LoggerFactory.getLogger(CustomAccessControlFilter.class);

//    private SessionManager sessionManager;
//    private Cache<String, Deque<Session>> cache;
//    void setSessionManager(SessionManager sessionManager) {
//        this.sessionManager = sessionManager;
//    }
//
//    //设置Cache的key的前缀
//    void setCacheManager(CacheManager cacheManager) {
//        this.cache = cacheManager.getCache("shiro_redis_cache");
//    }
//    private void deleteCacheSession(Subject subject) {
//        if (subject.getPrincipal() != null) {
//            String username = subject.getPrincipal().toString();
//            Deque<Session> deque = cache.get(username);
//            if (null != deque)
//                deque.remove(subject.getSession());
//        }
//    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        Subject subject = getSubject(request, response);
        if (!subject.isAuthenticated() && !subject.isRemembered()) {
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().print(JsonParse.objectToJsonString(StatusMsg.needToLogin));
//            deleteCacheSession(subject);
            return false;
        }
        return true;
//        Session session = subject.getSession();
//        String username = subject.getPrincipal().toString();
//
//        //读取缓存   没有就存入
//        Deque<Session> deque = cache.get(username);
//
//        if (null == deque) {
//            deque = new ArrayDeque<>();
//            cache.put(username, deque);
//        }
//
//        //如果队列里没有此sessionId，且用户没有被踢出；放入队列
//        String kickAttrKeyName = "kick";
//        Boolean isContain = false;
//        for (Session tempSession : deque)
//            if (tempSession.getId() == session.getId()) {
//                isContain = true;
//                session = tempSession;
//            }
//        if (!isContain && session.getAttribute(kickAttrKeyName) == null) {
//            //将sessionId存入队列
//            deque.push(session);
//            //将用户的sessionId队列缓存
//            cache.put(username, deque);
//        }

        //如果队列里的sessionId数超出最大会话数，开始踢人
        //同一个帐号最大会话数 默认1
//        int maxSession = 1;
//        while (deque.size() > maxSession) {
//            Session kickSession = deque.removeLast();
//            //踢出后再更新下缓存队列
//            cache.put(username, deque);
//            //获取被踢出的sessionId的session对象
//            try {
//                if (kickSession != null && kickSession != session)
//                    kickSession.setAttribute(kickAttrKeyName, true);
//            } catch (UnknownSessionException use) {
//                logger.info("Unknown session exception");
//                break;
//            }
//        }
        //如果被踢出了，直接退出，重定向到踢出后的地址
//        if (session.getAttribute(kickAttrKeyName) != null && (Boolean) session.getAttribute(kickAttrKeyName)) {
//            //会话被踢出了
//            subject.logout();
//            saveRequest(request);
//            deleteCacheSession(subject);
//            response.setContentType("application/json;charset=UTF-8");
//            response.getWriter().print(JsonParse.objectToJsonString(StatusMsg.overMaxSessionNumber));
//            return false;
//        }
    }

    @Override
    public boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
//        httpServletResponse.setHeader("Access-Control-Allow-Origin", httpServletRequest.getHeader("Origin"));
//        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
//        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.onPreHandle(request, response, mappedValue);
    }
}
