var ydbaobao = {};

ydbaobao.post = function(o) {
    o.method = o.method || "post"; // Set method to post by default if not specified.

    // The rest of this code assumes you are not using a library.
    // It can be made less wordy if you use one.
    var form = document.createElement("form");
    form.setAttribute("method", o.method);
    form.setAttribute("action", o.path);

    for(var key in o.params) {
        if(o.params.hasOwnProperty(key)) {
            var hiddenField = document.createElement("input");
            hiddenField.setAttribute("type", "hidden");
            hiddenField.setAttribute("name", key);
            hiddenField.setAttribute("value", o.params[key]);

            form.appendChild(hiddenField);
         }
    }

    document.body.appendChild(form);
    form.submit();
}

ydbaobao.ajax = function(o) {
	if (o.method === undefined || o.url === undefined || o.success === undefined) {
		return;
	}
	var req = new XMLHttpRequest();
	req.onreadystatechange = function() {
		if (req.readyState == 4) {
			if (req.status == 200) {
				o.success(req);
			} else {
				document.body.innerHTML = req.responseText;
			}
		}
	};
	if (o.async === undefined) o.async = true;
	req.open(o.method, o.url, o.async);
	if (o.method.toLowerCase() == 'post' || o.method.toLowerCase() == 'put' || o.method.toLowerCase() == 'delete') {
		req.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
	}
	req.send(o.param);
};

ydbaobao.ajaxFile = function(o) {
	if (o.method === undefined || o.url === undefined || o.success === undefined) {
		return;
	}
	var req = new XMLHttpRequest();
	req.onreadystatechange = function() {
		if (req.readyState == 4) {
			if (req.status == 200) {
				o.success(req);
			} else {
				document.body.innerHTML = req.responseText;
			}
		}
	};
	if (o.async === undefined) o.async = true;
	req.open(o.method, o.url, o.async);
	if (o.method.toLowerCase() == 'post' || o.method.toLowerCase() == 'put' || o.method.toLowerCase() == 'delete') {
		req.setRequestHeader('Content-type', 'multipart/form-data');
	}
	req.send(o.param);
};

ydbaobao.createElement = function(o) {
	var el = document.createElement(o.name);
	if (o.attrs !== undefined) {
		for ( var attr in o.attrs) {
			if (o.attrs.hasOwnProperty(attr)) {
				el.setAttribute(attr, o.attrs[attr]);
			}
		}
	}
	if (o.content) {
		el.innerHTML = o.content;
	}
	return el;
};

Element.prototype.remove = function() {
	if (this.parentElement !== null)
		this.parentElement.removeChild(this);
};

Element.prototype.show = function() {
	this.style.display = "block";
};

Element.prototype.hide = function() {
	this.style.display = "none";
};

Element.prototype.parentNodeSelector = function(cssquery) {
	var type = (cssquery[0] === "#") ? "id" : "className";
	var name = cssquery.split(cssquery[0])[1];
	var pNode = this.parentNode;
	while (pNode != null) {
		if (pNode[type] === name) {
			return pNode;
		}
		pNode = pNode.parentNode;
	}
	return null;
}

function isNumberKey(evt) {
    var charCode = (evt.which) ? evt.which : event.keyCode
    return !(charCode > 31 && (charCode < 48 || charCode > 57));
};