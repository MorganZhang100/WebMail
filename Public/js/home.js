if(location.hash == "") {
    window.location = "home#inbox/0";
    showUserFolders("user_folders");
}

$("#check_mail_button").click(
    function() {
        $("#unread_button").remove();

        $.post(
            "HomeCheckEmailPost",
            {},
            function(result)
            {
                $("#down_right_big").empty();
                var i;
                for(i=0; i<result.mailAmount; i++) {
                    $("#down_right_big").prepend("<div class=\"row\"><a class=\"email_brief\" href=\"#detail/" + result.mailsBrief[i].mail_id + "\" id=\"detail_" + result.mailsBrief[i].mail_id + "\" ><div><span class=\"col-lg-3 email_brief_span\" >" + result.mailsBrief[i].from_name + "</span><span class=\"col-lg-3 email_brief_span\" >" + result.mailsBrief[i].subject + "</span><span class=\"col-lg-6 email_brief_span\" >" + result.mailsBrief[i].body + "</span></div></a></div>");
                }

                $("#pre_button").attr("href","#inbox/0");
                $("#aft_button").attr("href","#inbox/1");
            },
            "json"
        );
    }
);

window.onhashchange = function() {

    $("#unread_button").remove();
    $("#delete_button").remove();
    $("#empty_trash_button").remove();
    $("#foldersDiv").remove();

    var hashStr = location.hash.replace("#","");
    var hashKey = hashStr.split("/")[0];
    var hashValue = hashStr.split("/")[1];

    if(hashKey == "unread") {
        $.post(
            "HomeUnReadPost",
            {
                mail_id : hashValue
            },
            function(result)
            {},
            "json"
        );
    }

    if(hashKey == "delete") {
        $.post(
            "HomeDeleteToTrashPost",
            {
                mail_id : hashValue
            },
            function(result)
            {
                window.location = "home#inbox/0";
            },
            "json"
        );
    }

    if(hashKey == "emptyall") {
        $.post(
            "HomeEmptyAllTrashPost",
            {},
            function(result)
            {
                window.location = "home#trash/0";
            },
            "json"
        );
    }

    if(hashKey == "addFolder") {
        $.post(
            "HomeAddNewFolderPost",
            {
                name : $("#newFolderInput").val()
            },
            function(result)
            {
                if(result.state == "done") {
                    showUserFolders("user_folders");
                    window.location = "#inbox/0";
                }
            },
            "json"
        );
    }

    if(hashKey == "detail") {
        $.post(
            "HomeEmailDetail",
            {
                mail_id : hashValue
            },
            function(result)
            {
                $("#down_right_big").empty();
                $("#down_right_big").prepend("<div class=\"row\" id=\"email_detail\" ></div>");
                $("#email_detail").prepend("<div class=\"email_attachment\" id=\"email_attachment\"></div>");
                $("#email_detail").prepend("<div class=\"email_body\" >" + result.body + "</div>");
                $("#email_detail").prepend("<div class=\"email_head\" ><span class=\"col-lg-6\" >" + result.from_name + "&lt;" + result.from_address + "&gt; </span><span class=\"col-lg-6\" > To me &lt;" + result.to_address + "&gt;</span></div>");
                $("#email_detail").prepend("<div class=\"email_subject\"><span class=\"col-lg-12\" >" + result.subject + "</span></div>");

                $("#mid_right_big_left_buttons").prepend("<a href=\"#unread/" + hashValue + "\" class=\"btn btn-default mid_right_buttons\" id=\"unread_button\">UnRead</a>");
                if(result.mail_state == 0) $("#mid_right_big_left_buttons").prepend("<a href=\"#delete/" + hashValue + "\" class=\"btn btn-default mid_right_buttons\" id=\"delete_button\">Delete</a>");
                $("#mid_right_big_left_buttons").prepend(
                    "<div class=\"btn-group\" id=\"foldersDiv\">" +
                        "<button type=\"button\" class=\"btn btn-default dropdown-toggle\" data-toggle=\"dropdown\" aria-expanded=\"false\">" +
                            "Folder <span class=\"caret\"></span>" +
                        "</button>" +
                        "<ul class=\"dropdown-menu\" role=\"menu\" id=\"folderMenu\">" +
                        "</ul>" +
                    "</div>"
                );
                showUserFolders("folderMenu");

                var i;
                for(i=0; i<result.attachmentAmount; i++) {
                    $("#email_attachment").prepend("<div><a href=\"/Public/tem/" + result.attachments[i].id + "\" target=\"_blank\">" + result.attachments[i].name + "</a></div>");
                }
            },
            "json"
        );
    }

    if(hashKey == "edit") {
         $.post(
             "HomeEditUserInformationPost",
             {},
             function(result)
             {
                 $("#down_right_big").empty();
                 $("#down_right_big").prepend("<div class=\"row\" id=\"user_information\" ></div>");
                 $("#user_information").prepend(
                     "<div class=\"user_left col-lg-6\" >" +
                             "<label for=\"NickName\" class=\"change_user_information_labels\">Nick Name</label>" +
                             "<input type=\"text\" class=\"form-control\" id=\"nickName\" name=\"nickName\" value=" + result.nick_name + " >" +
                             "<label for=\"NickName\" class=\"change_user_information_labels\">Login Name</label>" +
                             "<input type=\"text\" class=\"form-control\" id=\"loginName\" name=\"loginName\" value=" + result.login_name + " >" +
                             "<label for=\"NickName\" class=\"change_user_information_labels\">Email Address</label>" +
                             "<input type=\"text\" class=\"form-control\" id=\"emailAddress\" name=\"emailAddress\" value=" + result.email_address + " >" +
                             "<label for=\"NickName\" class=\"change_user_information_labels\">Email Password</label>" +
                             "<input type=\"text\" class=\"form-control\" id=\"emailPwd\" name=\"emailPwd\" value=" + result.email_pwd + " >" +
                             "<label for=\"NickName\" class=\"change_user_information_labels\">POP Server Address</label>" +
                             "<input type=\"text\" class=\"form-control\" id=\"popServer\" name=\"popServer\" value=" + result.pop_server + " >" +
                             "<label for=\"NickName\" class=\"change_user_information_labels\">POP Server Port</label>" +
                             "<input type=\"text\" class=\"form-control\" id=\"popPort\" name=\"popPort\" value=" + result.pop_port + " >" +
                             "<label for=\"NickName\" class=\"change_user_information_labels\">SMTP Server Address</label>" +
                             "<input type=\"text\" class=\"form-control\" id=\"smtpServer\" name=\"smtpServer\" value=" + result.smtp_server + " >" +
                             "<label for=\"NickName\" class=\"change_user_information_labels\">SMTP Server Port</label>" +
                             "<input type=\"text\" class=\"form-control\" id=\"smtpPort\" name=\"smtpPort\" value=" + result.smtp_port + " >" +

                             "<a class=\"btn btn-primary change_user_information_buttons\" id=\"user_information_change_button\" onclick=\"saveUserInformationChange()\">Save Change</a> " +
                     "</div>" +

                     "<div class=\"user_left col-lg-6\" >" +
                             "<label for=\"NickName\" class=\"change_user_information_labels\">Old Password</label>" +
                             "<input type=\"text\" class=\"form-control\" id=\"oldPassword\" name=\"oldPassword\" >" +
                             "<label for=\"NickName\" class=\"change_user_information_labels\">New Password</label>" +
                             "<input type=\"text\" class=\"form-control\" id=\"newPasswordOne\" name=\"newPasswordOne\" >" +
                             "<label for=\"NickName\" class=\"change_user_information_labels\">Repeat New Password</label>" +
                             "<input type=\"text\" class=\"form-control\" id=\"newPasswordTwo\" name=\"newPasswordTwo\" >" +

                             "<a class=\"btn btn-primary change_user_information_buttons\" id=\"user_pwd_change_button\" onclick=\"saveUserPwdChange()\">Change Password</a> " +
                     "</div>"
                 );
             },
             "json"
         );
    }

    if(hashKey == "inbox") {
        if(hashValue == undefined) {
            hashValue = 0;
        }
        prePageNumber = hashValue > 0 ? hashValue -1 : 0;
        aftPageNumber = parseInt(hashValue) + 1;

        $.post(
            "HomeInboxPost",
            {
                pageNumber: hashValue,
                folderId: 0
            },
            function(result)
            {
                $("#down_right_big").empty();
                var i;
                for(i=0; i<result.mailAmount; i++) {
                    $("#down_right_big").prepend("<div class=\"row\"><a class=\"email_brief\" href=\"#detail/" + result.mailsBrief[i].mail_id + "\" id=\"detail_" + result.mailsBrief[i].mail_id + "\" ><div><span class=\"col-lg-3 email_brief_span\" >" + result.mailsBrief[i].from_name + "</span><span class=\"col-lg-3 email_brief_span\" >" + result.mailsBrief[i].subject + "</span><span class=\"col-lg-6 email_brief_span\" >" + result.mailsBrief[i].body + "</span></div></a></div>");

                    if(result.mailsBrief[i].read_flag == 0) $("#detail_" + result.mailsBrief[i].mail_id).addClass("unread");
                }

                $("#pre_button").attr("href","#inbox/" + prePageNumber);
                $("#aft_button").attr("href","#inbox/" + aftPageNumber);

            },
            "json"
        );
    }

    if(hashKey == "folder") {
        $.post(
            "HomeInboxPost",
            {
                pageNumber: 0,
                folderId: hashValue
            },
            function(result)
            {
                $("#down_right_big").empty();
                var i;
                for(i=0; i<result.mailAmount; i++) {
                    $("#down_right_big").prepend("<div class=\"row\"><a class=\"email_brief\" href=\"#detail/" + result.mailsBrief[i].mail_id + "\" id=\"detail_" + result.mailsBrief[i].mail_id + "\" ><div><span class=\"col-lg-3 email_brief_span\" >" + result.mailsBrief[i].from_name + "</span><span class=\"col-lg-3 email_brief_span\" >" + result.mailsBrief[i].subject + "</span><span class=\"col-lg-6 email_brief_span\" >" + result.mailsBrief[i].body + "</span></div></a></div>");

                    if(result.mailsBrief[i].read_flag == 0) $("#detail_" + result.mailsBrief[i].mail_id).addClass("unread");
                }
            },
            "json"
        );
    }

    if(hashKey == "trash") {
        if(hashValue == undefined) {
            hashValue = 0;
        }
        prePageNumber = hashValue > 0 ? hashValue -1 : 0;
        aftPageNumber = parseInt(hashValue) + 1;

        $.post(
            "HomeTrashFolderPost",
            {
                pageNumber: hashValue
            },
            function(result)
            {
                $("#down_right_big").empty();
                var i;
                for(i=0; i<result.mailAmount; i++) {
                    $("#down_right_big").prepend("<div class=\"row\"><a class=\"email_brief\" href=\"#detail/" + result.mailsBrief[i].mail_id + "\" id=\"detail_" + result.mailsBrief[i].mail_id + "\" ><div><span class=\"col-lg-3 email_brief_span\" >" + result.mailsBrief[i].from_name + "</span><span class=\"col-lg-3 email_brief_span\" >" + result.mailsBrief[i].subject + "</span><span class=\"col-lg-6 email_brief_span\" >" + result.mailsBrief[i].body + "</span></div></a></div>");

                    if(result.mailsBrief[i].read_flag == 0) $("#detail_" + result.mailsBrief[i].mail_id).addClass("unread");
                }
                $("#mid_right_big_left_buttons").prepend("<a href=\"#emptyall\" class=\"btn btn-default mid_right_buttons\" id=\"empty_trash_button\">Empty</a>");

                $("#pre_button").attr("href","#trash/" + prePageNumber);
                $("#aft_button").attr("href","#trash/" + aftPageNumber);

            },
            "json"
        );
    }

    if(hashKey == "logout") {
        $.post(
            "LogoutPost",
            {},
            function(result)
            {
                window.location = "/";
            },
            "json"
        );
    }

    if(hashKey == "compose") {
        $("#down_right_big").empty();
        $("#down_right_big").prepend("<div class=\"row\" id=\"compose_detail\" ></div>");
        $("#compose_detail").prepend("<div class=\"col-lg-12 compose_down\"><a class=\"btn btn-primary col-lg-2\" href=\"#send\" id=\"send_button\">Send</a></div>");
        $("#compose_detail").prepend("<textarea class=\"compose_body col-lg-12 form-control\" rows=\"20\" id=\"body\"/></textarea>");
        $("#compose_detail").prepend("<input type=\"text\" class=\"compose_head col-lg-12 form-control\" placeholder=\"Subject:\" id=\"subject\"/>");
        $("#compose_detail").prepend("<input type=\"text\" class=\"compose_head col-lg-12 form-control\" placeholder=\"To:\" id=\"toAddress\"/>");
    }

    if(hashKey == "send") {
        $.post(
            "HomeComposePost",
            {
                subject : $("#subject").val(),
                toAddress : $("#toAddress").val(),
                body : $("#body").val()
            },
            function(result)
            {
                alert(result.success);
            },
            "json"
        );
    }
}

function saveUserInformationChange() {
    $.post(
        "HomeChangeUserInformationPost",
        {
            loginName: $("#loginName").val(),
            nickName: $("#nickName").val(),
            emailAddress: $("#emailAddress").val(),
            emailPwd: $("#emailPwd").val(),
            popServer: $("#popServer").val(),
            popPort: $("#popPort").val(),
            smtpServer: $("#smtpServer").val(),
            smtpPort: $("#smtpPort").val()
        },
        function(result)
        {
            if(result.state == "success") alert("Change Success");
            window.location = "#inbox/0";
        },
        "json"
    );
}

function saveUserPwdChange() {
    if($("#newPasswordOne").val() != $("#newPasswordTwo").val())
    {
        alert("New Password Incorrect!");
    }
    else {
        $.post(
            "HomeChangeUserPwdPost",
            {
                oldPwd: $("#oldPassword").val(),
                newPwd: $("#newPasswordOne").val()
            },
            function(result)
            {
                if(result.state == "success") {
                    alert("Change Success");
                    window.location = "#inbox/0";
                }
                else alert(result.state);
            },
            "json"
        );
    }
}

function showUserFolders(showPlace) {
    $.post(
        "HomeShowUserFoldersPost",
        {},
        function(result)
        {
            if(result.state == "done") {
                if(showPlace == "user_folders") $("#user_folders").empty();
                var i;
                for(i=0; i<result.folderAmount; i++) {
                    if(showPlace == "user_folders") $("#user_folders").prepend("<div class=\"left_buttons\"><a href=\"#folder/" + result.folders[i].id + "\">" + result.folders[i].name + "</a></div>");
                    if(showPlace == "folderMenu") $("#folderMenu").prepend("<li><a onclick=changeFolder(" + result.folders[i].id + ")>" + result.folders[i].name + "</a></li>");
                }
            }
        },
        "json"
    );
}

function changeFolder(aimFolderId) {
    var hashStr = location.hash.replace("#","");
    var hashKey = hashStr.split("/")[0];
    var id = hashStr.split("/")[1];

    $.post(
        "HomeChangeFolderPost",
        {
            mailId: id,
            folderId: aimFolderId
        },
        function(result)
        {
            if(result.state == "done") {
                window.location = "#folder/" + aimFolderId;
            }
        },
        "json"
    );
}