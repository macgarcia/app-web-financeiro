drop procedure if exists desfazer_fechamento_mes;

delimiter //

create procedure desfazer_fechamento_mes(in id_calculo_mensal_p int)
start_processo: begin
    --
    update divida d
      set d.calculo_mensal_id = null
     where d.calculo_mensal_id = id_calculo_mensal_p;
    --
    delete from calculo_mensal where id = id_calculo_mensal_p;
    --
    commit;
    --
end;
//

delimiter ;