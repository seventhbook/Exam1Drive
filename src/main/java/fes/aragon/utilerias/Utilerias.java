package fes.aragon.utilerias;
import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
public class Utilerias {
	public static String getRutaImagenes(String recurso) {
		return getServletContext().getRealPath(recurso);
	}

	public static ServletContext getServletContext() {
		return (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
	}

	public static ExternalContext getExternalContext() {
		FacesContext fc = FacesContext.getCurrentInstance();
		return fc.getExternalContext();
	}

	public static HttpSession getHttpSession(boolean create) {
		return (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(create);
	}

	
	public static void setManagedBeanInSession(String beanName, Object managedBean) {
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(beanName, managedBean);
	}


	public static Object getManagedBean(String beanName, Object objeto) {
		FacesContext contexto = FacesContext.getCurrentInstance();
		ELContext elContext = contexto.getELContext();
		Application application = contexto.getApplication();
		ExpressionFactory expressionFactory = application.getExpressionFactory();
		ValueExpression ve = expressionFactory.createValueExpression(elContext, getJsfEl(beanName), Object.class);
		return ve.getValue(elContext);

	}

	public static void quitarManagerBean(String beanName, Object objeto) {
		FacesContext contexto = FacesContext.getCurrentInstance();
		ELContext elContext = contexto.getELContext();
		Application application = contexto.getApplication();
		ExpressionFactory expressionFactory = application.getExpressionFactory();
		ValueExpression ve = expressionFactory.createValueExpression(elContext, getJsfEl(beanName), Object.class);
		ve.setValue(elContext, null);
	}

	public static String getRequestParameter(String name) {
		return (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(name);
	}

	private static String getJsfEl(String value) {
		return "#{" + value + "}";
	}
}
