<%@ include file="/WEB-INF/jsp/header.jspf" %>
<h1><c:out value="${judul}"/></h1>
<div class="card" style="max-width:520px">
    <form method="post" action="${ctx}/produk">
        <input type="hidden" name="action" value="save">
        <input type="hidden" name="id" value="${p.id}">
        <label>Kode / Barcode</label>
        <input name="kode" value="<c:out value='${p.kode}'/>" required>
        <label>Nama Produk</label>
        <input name="nama" value="<c:out value='${p.nama}'/>" required>
        <label>Harga (Rp)</label>
        <input name="harga" type="number" min="0" value="${p.harga}" required>
        <label>Stok</label>
        <input name="stok" type="number" min="0" value="${p.stok}" required>
        <p>
            <button class="btn" type="submit">Simpan</button>
            <a class="btn gray" href="${ctx}/produk">Batal</a>
        </p>
    </form>
</div>
<%@ include file="/WEB-INF/jsp/footer.jspf" %>
