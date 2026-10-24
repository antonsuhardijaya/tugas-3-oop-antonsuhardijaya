<%@ include file="/WEB-INF/jsp/header.jspf" %>
<h1>Dashboard</h1>
<div class="grid">
    <div class="stat"><small>Penjualan hari ini</small><b>Rp <fmt:formatNumber value="${penjualanHariIni}"/></b></div>
    <div class="stat"><small>Transaksi hari ini</small><b><c:out value="${transaksiHariIni}"/></b></div>
    <div class="stat"><small>Total produk</small><b><c:out value="${totalProduk}"/></b></div>
    <div class="stat"><small>Stok menipis (‚â)</small><b><c:out value="${stokMenipis.size()}"/></b></div>
</div>
<div class="card">
    <a class="btn" href="${ctx}/kasir">Mulai Transaksi</a>
</div>
<div class="card">
    <h3 style="margin-top:0">Produk dengan stok menipis</h3>
    <c:choose>
        <c:when test="${empty stokMenipis}"><p>Semua stok aman üëç</p></c:when>
        <c:otherwise>
            <table>
                <tr><th>Kode</th><th>Nama</th><th class="r">Stok</th></tr>
                <c:forEach var="p" items="${stokMenipis}">
                    <tr><td><c:out value="${p.kode}"/></td><td><c:out value="${p.nama}"/></td>
                        <td class="r"><span class="badge"><c:out value="${p.stok}"/></span></td></tr>
                </c:forEach>
            </table>
        </c:otherwise>
    </c:choose>
</div>
<%@ include file="/WEB-INF/jsp/footer.jspf" %>
