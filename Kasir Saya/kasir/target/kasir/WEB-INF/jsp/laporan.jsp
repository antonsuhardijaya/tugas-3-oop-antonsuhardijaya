<%@ include file="/WEB-INF/jsp/header.jspf" %>
<h1>Laporan Penjualan</h1>
<div class="card">
    <form class="row" method="get" action="${ctx}/laporan">
        <label style="margin:0">Dari</label><input type="date" name="dari" value="${dari}" style="max-width:170px">
        <label style="margin:0">Sampai</label><input type="date" name="sampai" value="${sampai}" style="max-width:170px">
        <button class="btn" type="submit">Tampilkan</button>
    </form>
    <div class="grid">
        <div class="stat"><small>Jumlah transaksi</small><b><c:out value="${daftar.size()}"/></b></div>
        <div class="stat"><small>Total penjualan</small><b>Rp <fmt:formatNumber value="${omzet}"/></b></div>
    </div>
    <table>
        <tr><th>No. Faktur</th><th>Tanggal</th><th>Kasir</th><th class="r">Total</th><th></th></tr>
        <c:forEach var="t" items="${daftar}">
            <tr>
                <td><c:out value="${t.noFaktur}"/></td>
                <td><fmt:formatDate value="${t.tanggal}" pattern="dd/MM/yyyy HH:mm"/></td>
                <td><c:out value="${t.kasir}"/></td>
                <td class="r">Rp <fmt:formatNumber value="${t.total}"/></td>
                <td><a class="btn sm gray" href="${ctx}/struk?id=${t.id}" target="_blank">Struk</a></td>
            </tr>
        </c:forEach>
        <c:if test="${empty daftar}"><tr><td colspan="5">Tidak ada transaksi pada rentang ini.</td></tr></c:if>
    </table>
</div>
<%@ include file="/WEB-INF/jsp/footer.jspf" %>
