<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="id_ID"/>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="id">
<head>
    <meta charset="UTF-8">
    <title>Struk <c:out value="${t.noFaktur}"/></title>
    <link rel="stylesheet" href="${ctx}/css/style.css">
</head>
<body>
<div class="struk">
    <h2>TOKO SAYA</h2>
    <p>Jl. Contoh No. 1</p>
    <hr>
    <p style="text-align:left">No&nbsp;&nbsp;: <c:out value="${t.noFaktur}"/></p>
    <p style="text-align:left">Tgl : <fmt:formatDate value="${t.tanggal}" pattern="dd/MM/yyyy HH:mm"/></p>
    <p style="text-align:left">Kasir: <c:out value="${t.kasir}"/></p>
    <hr>
    <table>
        <c:forEach var="i" items="${t.items}">
            <tr><td colspan="2"><c:out value="${i.nama}"/></td></tr>
            <tr><td><c:out value="${i.qty}"/> x <fmt:formatNumber value="${i.harga}"/></td>
                <td style="text-align:right"><fmt:formatNumber value="${i.subtotal}"/></td></tr>
        </c:forEach>
    </table>
    <hr>
    <table>
        <tr><td>Total</td><td style="text-align:right"><b>Rp <fmt:formatNumber value="${t.total}"/></b></td></tr>
        <tr><td>Bayar</td><td style="text-align:right">Rp <fmt:formatNumber value="${t.bayar}"/></td></tr>
        <tr><td>Kembali</td><td style="text-align:right">Rp <fmt:formatNumber value="${t.kembali}"/></td></tr>
    </table>
    <hr>
    <p>Terima kasih atas kunjungan Anda</p>
</div>
<div class="noprint">
    <button class="btn" onclick="window.print()">🖨 Cetak</button>
    <a class="btn gray" href="${ctx}/kasir">Transaksi Baru</a>
</div>
</body>
</html>
