package utf8.citicup.shiro;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utf8.citicup.service.util.JsonParse;
import utf8.citicup.service.util.StatusMsg;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.Deque;

class CustomAccessControlFilter extends AccessControlFilter {

    private SessionManager sessionManager;
    private Cache<String, Deque<Session>> cache;
    private Logger logger = LoggerFactory.getLogger(CustomAccessControlFilter.class);

    void setSessionManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    //设置Cache的key的前缀
    void setCacheManager(CacheManager cacheManager) {
        this.cache = cacheManager.getCache("shiro_redis_cache");
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        logger.info("Access is allowed");
        return false;
    }

//    private void deleteCacheSession(Subject subject) {
//        if (subject.getPrincipal() != null) {
//            String username = subject.getPrincipal().toString();
//            Deque<Session> deque = cache.get(username);
//            if (null != deque)
//                deque.remove(subject.getSession());
//        }
//    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        Subject subject = getSubject(request, response);
        logger.info(subject.getSession().getId().toString());
        if (!subject.isAuthenticated() && !subject.isRemembered()) {
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().print(JsonParse.objectToJsonString(StatusMsg.needToLogin));
//            deleteCacheSession(subject);
            return false;
        }
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
        return true;
    }
}
