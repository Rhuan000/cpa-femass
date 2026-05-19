alter table if exists VALIDACAO
    add column if not exists ACEITE_TERMOS_CONDICOES_SERVICO boolean not null default false;
