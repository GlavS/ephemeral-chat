Для запуска приложений установить переменные окружения:
 - для main-ephe-chat: CHAT_USERNAME=user;CHAT_PASSWORD=pass
 - для panel-admin: PANEL_ADMIN=adm;PANEL_PASSWORD=pass

Порядок запуска на VPS:
 - `docker pull glavs/ephemeral-chat`
 - ```
    docker run --rm -d -p 23443:23443 \
    -v /var/run:/var/run \
    -v /home/sergey/cert:/home/sergey/tmp \
    -e PANEL_ADMIN=adm \
    -e PANEL_PASSWORD=pass \
     glavs/admin-panel
   ```