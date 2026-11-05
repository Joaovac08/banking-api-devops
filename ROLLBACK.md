# Plano de Rollback (Reversão)

Este documento define o procedimento para reverter a aplicação e a API em caso de falha crítica após um deploy.

## 1. Rollback da Aplicação (Docker)
A imagem é versionada no Docker Hub com o número do build e o hash do commit (ex: v14-abc1234).

**Passo a passo:**
1. Identifique a última tag estável anterior no Docker Hub.
2. No arquivo docker-compose.yml da VM, altere a imagem para a tag anterior.
3. Reinicie o container com docker compose up -d.

## 2. Rollback da API no WSO2
Se a API publicada no WSO2 apresentar falhas, o procedimento é:

1. Acesse o Publisher do WSO2 (https://localhost:9443/publisher).
2. Localize a API BankingAPI.
3. Clique em Deprecate ou Unpublish para tirar a versão com falha do ar.
4. Se houver uma versão anterior, publique-a novamente.
5. (Opcional) Via apictl, importe novamente o projeto com a versão estável anterior.

## 3. Rollback do Pipeline (Jenkins)
Se o build atual quebrou a branch main:

1. No GitHub, reverta o commit que causou o problema com git revert.
2. Faça o git push origin main.
3. O Jenkins detectará a mudança automaticamente e rodará o build com o código antigo.
