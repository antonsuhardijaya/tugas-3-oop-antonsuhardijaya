<%@ include file="/WEB-INF/jsp/header.jspf" %>
<h1>Data Produk</h1>
<div class="card">
    <div class="row">
        <form class="row" method="get" action="${ctx}/produk" style="flex:1;margin:0">
            <input name="q" value="<c:out value='${param.q}'/>" placeholder="Cari nama / kode produk...">
            <button class="btn" type="submit">Cari</button>
        </form>
        <a class="btn green" style="width:auto;font-size:14px;padding:9px 16px" href="${ctx}/produk?action=form">+ Tambah Produk</a>
    </div>
    <table>
        <tr><th>Kode</th><th>Nama</th><th class="r">Harga</th><th class="r">Stok</th><th>Aksi</th></tr>
        <c:forEach var="p" items="${daftar}">
            <tr>
                <td><c:out value="${p.kode}"/></td>
                <td><c:out value="${p.nama}"/></td>
                <td class="r">Rp <fmt:formatNumber value="${p.harga}"/></td>
                <td class="r"><c:out value="${p.stok}"/> <c:if test="${p.stok <= 5}"><span class="badge">menipis</span></c:if></td>
                <td>
                    <a class="btn sm gray" href="${ctx}/produk?action=form&id=${p.id}">Edit</a>
                    <form class="inline" method="post" action="${ctx}/produk" onsubmit="return confirm('Hapus produk ini?')">
                        <input type="hidden" name="action" value="delete">
                        <input type="hidden" name="id" value="${p.id}">
                        <button class="btn sm red" type="submit">Hapus</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${empty daftar}"><tr><td colspan="5">Belum ada produk.</td></tr></c:if>
    </table>
</div>
<%@ include file="/WEB-INF/jsp/footer.jspf" %>
