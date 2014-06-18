$(function() {
 $(document).ready(function() {
    //alert('IN submit');
    //var url = "path/to/your/script.php"; // the script where you handle the form input.
    var str=""+Q+"";
    $.ajax({
			
			
            url: "http://localhost:8983/solr/Wikipedia/select?wt=json&indent=true&spellcheck=true&spellcheck.build=true&spellcheck.onlyMorePopular=true&spellcheck.collate=true&spellcheck.accuracy=0.7&spellcheck.q="+str,
            data: { 
                
				facet:'true',
                wt: 'json',
             }, 
             dataType: "jsonp",
             jsonp: 'json.wrf',
             
             success: function( data ) {
			     //alert(facet_counts);
			     var tr=data.spellcheck.suggestions;
				 
				 var qu=str;
				 var len=tr.length;
				 var res=tr[len-1];
				 
				 if(res!=null)
				 {
				 //alert('Did you mean "'+res+'" instead of  '+'"'+qu+'"');
				 $( "p" ).show( "slow" );
				 $( "p" ).text( 'Did you mean "'+res+'" instead of  '+'"'+qu+'"' );
                response(res);
				}
				
            }
          });

    return false; // avoid to execute the actual submit of the form.
});
}); 
