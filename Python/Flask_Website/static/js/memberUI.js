$(function(){
    var row1Index = 0;
    var row2Index = 0;
    var row3Index = 0;
    var row4Index = 0;
    var chosen;
    
    $('#r1').click(function(){
        if (row1Index > 2){
            rowIndex = 0;
        }
        chosen = $('#r1').children(".col").eq(row1Index);
        chosen.toggleClass('chosen');
        chosen.toggleClass('not-chosen');
        row1Index++;
    });

    $('#r2').click(function(){
        
        if (row2Index > 2){
            row2Index = 0;
        }
        chosen = $('#r2').children(".col").eq(row2Index);
        chosen.toggleClass('chosen');
        chosen.toggleClass('not-chosen');
        row2Index++;
    });

    $('#r3').click(function(){
        
        if (row3Index > 2){
            row3Index = 0;
        }
        chosen = $('#r3').children(".col").eq(row3Index);
        chosen.toggleClass('chosen');
        chosen.toggleClass('not-chosen');
        row3Index++;
    });

    $('#r4').click(function(){
        
        if (row4Index > 2){
            row4Index = 0;
        }
        chosen = $('#r4').children(".col").eq(row4Index);
        chosen.toggleClass('chosen');
        chosen.toggleClass('not-chosen');
        row4Index++;
    });

    
});