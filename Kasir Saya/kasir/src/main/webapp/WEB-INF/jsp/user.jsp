<%@ include file="/WEB-INF/jsp/header.jspf" %>
<h1>Manajemen User</h1>
<div class="pos">
    <div class="card">
        <table>
            <tr><th>Username</th><th>Nama</th><th>Role</th><th></th></tr>
            <c:forEach var="u" items="${daftar}">
                <tr>
                    <td><c:out value="${u.username}"/></td>
                    <td><c:out value="${u.nama}"/></td>
                    <td><c:out value="${u.role}"/></td>
                    <td>
                        <c:if test="${u.id != sessionScope.user.id}">
                            <form class="inline" method="post" action="${ctx}/user" onsubmit="return confirm('Hapus user ini?')">
                                <input type="hidden" name="action" value="delete">
                                <input type="hidden" name="id" value="${u.id}">
                                <button class="btn sm red" type="submit">Hapus</button>
                            </form>
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </div>
    <div class="card">
        <h3 style="margin-top:0">Tambah User</h3>
        <form method="post" action="${ctx}/user" autocomplete="off">
            <input type="hidden" name="action" value="save">
            <label>Username</label><input name="username" required>
            <label>Nama lengkap</label><input name="nama" required>
            <label>Password (min. 6 karakter)</label><input type="password" name="password" minlength="6" required>
            <label>Role</label>
            <select name="role"><option value="KASIR">KASIR</option><option value="ADMIN">ADMIN</option></select>
            <p><button class="btn" type="submit">Simpan</button></p>
        </form>
    </div>
</div>
<%@ include file="/WEB-INF/jsp/footer.jspf" %>
