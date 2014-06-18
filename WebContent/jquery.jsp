function f1()
{
$('#jq').addClass('abc');
$('#jq').text('Processing Query');
$('#jq').slideDown(2000);
$('#jq').slideUp(2000);
}
function f2()
{
$('#jq').addClass('abc');
$('#jq').text('Processing Query');
$('#jq').slideDown(2000);
$('#jq').slideUp(2000);
}
function f3()
{
$("input#shopName").autocomplete({
    source: "getShop.php",
    minLength: 2,
    select: function(event, ui) { 
        $("#theHidden").val(ui.item.id) 
    }
});
}
function f4()
{
$('#jq')
}
