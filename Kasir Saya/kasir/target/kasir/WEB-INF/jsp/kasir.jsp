<%@ include file="/WEB-INF/jsp/header.jspf" %>
<h1>Kasir</h1>
<div class="pos">
    <!-- KIRI: daftar produk -->
    <div class="card">
        <form class="row" method="get" action="${ctx}/kasir">
            <input name="q" value="<c:out value='${param.q}'/>" placeholder="Cari produk...">
            <button class="btn" type="submit">Cari</button>
        </form>
        <table>
            <tr><th>Kode</th><th>Nama</th><th class="r">Harga</th><th class="r">Stok</th><th></th></tr>
            <c:forEach var="p" items="${produk}">
                <tr>
                    <td><c:out value="${p.kode}"/></td>
                    <td><c:out value="${p.nama}"/></td>
                    <td class="r"><fmt:formatNumber value="${p.harga}"/></td>
                    <td class="r"><c:out value="${p.stok}"/></td>
                    <td>
                        <c:if test="${p.stok > 0}">
                            <form class="inline" method="post" action="${ctx}/kasir">
                                <input type="hidden" name="action" value="add">
                                <input type="hidden" name="id" value="${p.id}">
                                <button class="btn sm" type="submit">+ Tambah</button>
                            </form>
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty produk}"><tr><td colspan="5">Produk tidak ditemukan.</td></tr></c:if>
        </table>
    </div>

    <!-- KANAN: keranjang & pembayaran -->
    <div class="card">
        <form class="row" method="post" action="${ctx}/kasir">
            <input type="hidden" name="action" value="add">
            <input name="kode" placeholder="Scan barcode / ketik kode produk lalu Enter" autofocus autocomplete="off">
            <button class="btn" type="submit">Tambah</button>
        </form>

        <table>
            <tr><th>Item</th><th class="r">Harga</th><th>Qty</th><th class="r">Subtotal</th><th></th></tr>
            <c:forEach var="i" items="${items}">
                <tr>
                    <td><c:out value="${i.nama}"/></td>
                    <td class="r"><fmt:formatNumber value="${i.harga}"/></td>
                    <td>
                        <form class="inline" method="post" action="${ctx}/kasir">
                            <input type="hidden" name="action" value="set">
                            <input type="hidden" name="id" value="${i.produkId}">
                            <input class="qty" type="number" name="qty" min="0" value="${i.qty}" onchange="this.form.submit()">
                        </form>
                    </td>
                    <td class="r"><fmt:formatNumber value="${i.subtotal}"/></td>
                    <td>
                        <form class="inline" method="post" action="${ctx}/kasir">
                            <input type="hidden" name="action" value="remove">
                            <input type="hidden" name="id" value="${i.produkId}">
                            <button class="btn sm red" type="submit">hapus</button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty items}"><tr><td colspan="5">Keranjang kosong.</td></tr></c:if>
        </table>

        <div class="total">Total: Rp <fmt:formatNumber value="${total}"/></div>

        <c:if test="${not empty items}">
            <form method="post" action="${ctx}/kasir">
                <input type="hidden" name="action" value="pay">
                <label>Uang dibayar (Rp)</label>
                <div class="row">
                    <input id="bayar" name="bayar" type="number" min="0" required>
                    <button type="button" class="btn gray" onclick="uangPas()">Uang pas</button>
                </div>
                <div style="margin:8px 0 14px">Kembalian: <b id="kembali">Rp 0</b></div>
                <button class="btn green" type="submit">💰 Bayar &amp; Cetak Struk</button>
            </form>
            <form method="post" action="${ctx}/kasir" style="margin-top:10px">
                <input type="hidden" name="action" value="clear">
                <button class="btn red sm" type="submit">Kosongkan keranjang</button>
            </form>
            <script>
                var total = ${total};
                var b = document.getElementById('bayar'), k = document.getElementById('kembali');
                function fmt(n) { return 'Rp ' + n.toLocaleString('id-ID'); }
                b.addEventListener('input', function () { k.textContent = fmt(Math.max(0, (parseInt(b.value) || 0) - total)); });
                function uangPas() { b.value = total; b.dispatchEvent(new Event('input')); }
            </script>
        </c:if>
    </div>
</div>
<%@ include file="/WEB-INF/jsp/footer.jspf" %>
