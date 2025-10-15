-- =====================================================================
-- SCRIPT DML (Data Manipulation Language) PARA O SISTEMA GIED
-- INSERÇÃO DE DADOS DE EXEMPLO NAS TABELAS
-- =====================================================================

-- Inserção na tabela de domínio TIPO_MOVIMENTACAO
-- Justificativa: A regra de negócio prevê apenas dois tipos de movimentação, conforme o enum TipoMovimentacao.java.
INSERT INTO TIPO_MOVIMENTACAO (ID_TP_MOVIMENTACAO, DS_TIPO_MOVIMENTACAO) VALUES (1, 'ENTRADA');
INSERT INTO TIPO_MOVIMENTACAO (ID_TP_MOVIMENTACAO, DS_TIPO_MOVIMENTACAO) VALUES (2, 'RETIRADA');

-- Inserção de 5 usuários de exemplo
-- A senha para todos é "123", e o hash corresponde ao BCrypt usado na aplicação Java.
INSERT INTO USUARIO (ID_USUARIO, NM_USUARIO, DS_LOGIN, DS_SENHA_HASH, TP_USUARIO) VALUES (seq_usuario.NEXTVAL, 'Giulia Rocha', 'admin.gied', '$2b$10$Fg13SzlXujoyfhBlEOLB9ukqrdpGnqXOR7qt9jlemmyp3X7ep4QJO', 'ADMIN');
INSERT INTO USUARIO (ID_USUARIO, NM_USUARIO, DS_LOGIN, DS_SENHA_HASH, TP_USUARIO) VALUES (seq_usuario.NEXTVAL, 'Beatriz Martins', 'beatriz.m', '$2b$10$Fg13SzlXujoyfhBlEOLB9ukqrdpGnqXOR7qt9jlemmyp3X7ep4QJO', 'DEFAULT');
INSERT INTO USUARIO (ID_USUARIO, NM_USUARIO, DS_LOGIN, DS_SENHA_HASH, TP_USUARIO) VALUES (seq_usuario.NEXTVAL, 'Carlos Silva', 'carlos.s', '$2b$10$Fg13SzlXujoyfhBlEOLB9ukqrdpGnqXOR7qt9jlemmyp3X7ep4QJO', 'DEFAULT');
INSERT INTO USUARIO (ID_USUARIO, NM_USUARIO, DS_LOGIN, DS_SENHA_HASH, TP_USUARIO) VALUES (seq_usuario.NEXTVAL, 'Daniela Costa', 'daniela.c', '$2b$10$Fg13SzlXujoyfhBlEOLB9ukqrdpGnqXOR7qt9jlemmyp3X7ep4QJO', 'DEFAULT');
INSERT INTO USUARIO (ID_USUARIO, NM_USUARIO, DS_LOGIN, DS_SENHA_HASH, TP_USUARIO) VALUES (seq_usuario.NEXTVAL, 'Eduardo Lima', 'eduardo.l', '$2b$10$Fg13SzlXujoyfhBlEOLB9ukqrdpGnqXOR7qt9jlemmyp3X7ep4QJO', 'DEFAULT');

-- Inserção de 5 itens de exemplo
INSERT INTO ITEM (ID_ITEM, NM_ITEM, DS_ITEM, NR_NIVEL_MIN_ESTOQUE) VALUES (seq_item.NEXTVAL, 'Seringa Descartável 5ml', 'Seringa descartável com agulha 25x7mm, marca Medix', 50);
INSERT INTO ITEM (ID_ITEM, NM_ITEM, DS_ITEM, NR_NIVEL_MIN_ESTOQUE) VALUES (seq_item.NEXTVAL, 'Gaze Estéril 10x10cm', 'Pacote com 10 unidades de gaze estéril de algodão', 100);
INSERT INTO ITEM (ID_ITEM, NM_ITEM, DS_ITEM, NR_NIVEL_MIN_ESTOQUE) VALUES (seq_item.NEXTVAL, 'Álcool Etílico 70%', 'Frasco de 1L de álcool etílico 70% para assepsia', 30);
INSERT INTO ITEM (ID_ITEM, NM_ITEM, DS_ITEM, NR_NIVEL_MIN_ESTOQUE) VALUES (seq_item.NEXTVAL, 'Luva Cirúrgica (Par)', 'Par de luvas cirúrgicas de látex, tamanho M, estéril', 200);
INSERT INTO ITEM (ID_ITEM, NM_ITEM, DS_ITEM, NR_NIVEL_MIN_ESTOQUE) VALUES (seq_item.NEXTVAL, 'Cateter Intravenoso N22', 'Cateter periférico intravenoso número 22', 80);

-- Inserção de 5 lotes de exemplo (associados aos IDs dos itens criados acima: 1=Seringa, 2=Gaze, etc.)
INSERT INTO LOTE (ID_LOTE, ID_ITEM, NR_LOTE, DT_VALIDADE, NR_QUANTIDADE) VALUES (seq_lote.NEXTVAL, 1, 'SR2026-01A', TO_DATE('31/12/2026', 'DD/MM/YYYY'), 150);
INSERT INTO LOTE (ID_LOTE, ID_ITEM, NR_LOTE, DT_VALIDADE, NR_QUANTIDADE) VALUES (seq_lote.NEXTVAL, 1, 'SR2025-11B', TO_DATE('30/11/2025', 'DD/MM/YYYY'), 80);
INSERT INTO LOTE (ID_LOTE, ID_ITEM, NR_LOTE, DT_VALIDADE, NR_QUANTIDADE) VALUES (seq_lote.NEXTVAL, 2, 'GZ2027-05C', TO_DATE('31/05/2027', 'DD/MM/YYYY'), 200);
INSERT INTO LOTE (ID_LOTE, ID_ITEM, NR_LOTE, DT_VALIDADE, NR_QUANTIDADE) VALUES (seq_lote.NEXTVAL, 3, 'AL2028-10D', TO_DATE('31/10/2028', 'DD/MM/YYYY'), 50);
INSERT INTO LOTE (ID_LOTE, ID_ITEM, NR_LOTE, DT_VALIDADE, NR_QUANTIDADE) VALUES (seq_lote.NEXTVAL, 4, 'LV2026-08E', TO_DATE('31/08/2026', 'DD/MM/YYYY'), 500);

-- Inserção de 5 movimentações de exemplo para simular o histórico de transações
-- Para cada lote criado, registramos a sua ENTRADA. Depois, simulamos algumas RETIRADAS.
-- IDs: Usuario=1 (Admin), 2,3,4,5 (Default) | TipoMov=1(Entrada), 2(Retirada) | Lote=1,2,3...

-- Movimentações de ENTRADA para os lotes criados
INSERT INTO MOVIMENTACAO (ID_MOVIMENTACAO, ID_LOTE, ID_USUARIO, ID_TP_MOVIMENTACAO, DT_HR_MOVIMENTACAO, NR_QUANTIDADE_MOV) VALUES (seq_mov.NEXTVAL, 1, 1, 1, TO_TIMESTAMP_TZ('2025-09-01 10:00:00 -03:00', 'YYYY-MM-DD HH24:MI:SS TZH:TZM'), 150);
INSERT INTO MOVIMENTACAO (ID_MOVIMENTACAO, ID_LOTE, ID_USUARIO, ID_TP_MOVIMENTACAO, DT_HR_MOVIMENTACAO, NR_QUANTIDADE_MOV) VALUES (seq_mov.NEXTVAL, 2, 1, 1, TO_TIMESTAMP_TZ('2025-09-01 10:05:00 -03:00', 'YYYY-MM-DD HH24:MI:SS TZH:TZM'), 80);
INSERT INTO MOVIMENTACAO (ID_MOVIMENTACAO, ID_LOTE, ID_USUARIO, ID_TP_MOVIMENTACAO, DT_HR_MOVIMENTACAO, NR_QUANTIDADE_MOV) VALUES (seq_mov.NEXTVAL, 3, 2, 1, TO_TIMESTAMP_TZ('2025-09-02 09:00:00 -03:00', 'YYYY-MM-DD HH24:MI:SS TZH:TZM'), 200);

-- Movimentações de RETIRADA (consumo) de alguns lotes
-- Consumindo 20 unidades do lote de seringa que vence antes (ID_LOTE=2)
INSERT INTO MOVIMENTACAO (ID_MOVIMENTACAO, ID_LOTE, ID_USUARIO, ID_TP_MOVIMENTACAO, DT_HR_MOVIMENTACAO, NR_QUANTIDADE_MOV) VALUES (seq_mov.NEXTVAL, 2, 3, 2, TO_TIMESTAMP_TZ('2025-09-10 15:30:00 -03:00', 'YYYY-MM-DD HH24:MI:SS TZH:TZM'), 20);
-- Consumindo 50 unidades do lote de gaze (ID_LOTE=3)
INSERT INTO MOVIMENTACAO (ID_MOVIMENTACAO, ID_LOTE, ID_USUARIO, ID_TP_MOVIMENTACAO, DT_HR_MOVIMENTACAO, NR_QUANTIDADE_MOV) VALUES (seq_mov.NEXTVAL, 3, 4, 2, TO_TIMESTAMP_TZ('2025-09-11 11:00:00 -03:00', 'YYYY-MM-DD HH24:MI:SS TZH:TZM'), 50);

COMMIT;