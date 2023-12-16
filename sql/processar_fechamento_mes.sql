drop procedure if exists processar_fechamento_mes;

delimiter //

create procedure processar_fechamento_mes(in mes_selecionado_p varchar(10), in valor_saldo_mensal_p Double)
start_processo: begin
    --
    declare done int default false;
    declare vn_valor_resultante double;
    declare vn_valor_total_divida double;
    declare vn_valor_temp double;
    declare vn_id_calculo_mensal int;
    declare vn_fechamento_existente int;
    --
    declare dividas cursor for
        select d.valor
          from divida d
         where d.mes_divida = upper(mes_selecionado_p);
    declare continue handler for not found  set done = true;
    --
    set vn_valor_total_divida = 0;
    set vn_valor_resultante = 0;
    --
    -- Verificação se o mes ja esta fechado --
    select count(*)
      into vn_fechamento_existente
      from calculo_mensal c
     where c.mes = upper(mes_selecionado_p);
    --
    if vn_fechamento_existente = 1 then
        --
        signal sqlstate '45000' set message_text = 'Mês selecionado já esta fechado';
        --
    end if;
    --
    -- Somando todos os valores das dividas --
    open dividas;
    --
    start_loop: loop
        --
        fetch dividas into vn_valor_temp;
        if done then
            --
            leave start_loop;
            --
        end if;
        --
        set vn_valor_total_divida = vn_valor_total_divida + vn_valor_temp;
        --
    end loop;
    --
    close dividas;
    --
    set vn_valor_resultante = valor_saldo_mensal_p - vn_valor_total_divida;
    --
    insert into calculo_mensal(mes, valor_saldo_mensal, valor_resultante, valor_total_divida, situacao, ano)
    values(upper(mes_selecionado_p), valor_saldo_mensal_p,
        CAST(ROUND(vn_valor_resultante, 2) AS DECIMAL(10, 2)),
        vn_valor_total_divida, 'FECHADO', year(now()));
    --
    -- Recuperando o id gerado do fechamento mensal --
    select last_insert_id() into vn_id_calculo_mensal;
    --
    update divida d set d.calculo_mensal_id = vn_id_calculo_mensal
    where d.mes_divida = upper(mes_selecionado_p)
    and d.ano = year(now());
    --
    commit;
    --
end;
//

delimiter ;