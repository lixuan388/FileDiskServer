var AlertConfirm="<div class=\"modal fade\" id=\"AlertConfirm\" tabindex=\"-1\" role=\"dialog\" aria-labelledby=\"myModalLabel\">\r\n" + 
					"          <div class=\"modal-dialog\" role=\"document\" style=\"max-width: 360px;margin-left: auto;margin-right: auto;margin-top: 100px;\">\r\n" + 
					"            <div class=\"modal-content\">\r\n" + 
					"\r\n" + 
					"              <div class=\"modal-body\" style=\"min-height: 50px\" >\r\n" + 
					"                <span class=\"AlertConfirmText\"></span>\r\n" + 
					"              </div>\r\n" + 
					"              <div class=\"modal-footer\" style=\"padding: 5px;\">\r\n" + 
					"                <button type=\"button\" class=\"btn btn-default\"  data-dismiss=\"modal\">确定</button>\r\n" + 
					"              </div>\r\n" + 
					"            </div>\r\n" + 
					"          </div>\r\n" + 
					"        </div>  "


function alert(Text)
{
	if ($("#AlertConfirm").length==0)
	{
		$("body").append(AlertConfirm);
	}
	$("#AlertConfirm .AlertConfirmText").html(Text);
	$("#AlertConfirm").modal("show");
}
        