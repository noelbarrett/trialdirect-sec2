<html>
<body>
<h2>Hello World!</h2>

<div id="logout-link-holder">
    <ul class="nav">
        <li><a href="#/global/logins/current">logout</a></li>
        <li><a href="/global/logins/current">logout2</a></li>
    </ul>

</div>

<form method="post" action="/global/logins/current" class="inline">

    <button type="submit" name="submit_param" value="submit_value" >Logout</button>
</form>

</body>
</html>
