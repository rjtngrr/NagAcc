<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<spring:htmlEscape defaultHtmlEscape="true" />

<c:choose>
	<c:when test="${not empty sellers}">
		<div class="carousel__component">
            <div class="carousel__component--carousel js-owl-carousel js-owl-default">
                <c:forEach items="${sellers}" var="seller">
                    <div class="carousel__item">
                        <div class="carousel__item--thumb">
				            <img src="${fn:escapeXml(seller.sellerBanner.url)}" alt="${seller.sellerName}" title="${altTextHtml}"/>
                        </div>
                    </div>
                </c:forEach>
            </div>
		</div>
	</c:when>

	<c:otherwise>
	</c:otherwise>
</c:choose>

