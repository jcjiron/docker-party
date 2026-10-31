FROM php:8.3-apache

# WordPress se conecta a MySQL con la extensión mysqli
RUN docker-php-ext-install mysqli

# mod_rewrite: necesario para URLs bonitas (/mi-post en vez de /?p=123)
RUN a2enmod rewrite

COPY . /var/www/html/

# Apache corre como www-data; WordPress necesita escribir en wp-content
# (subir imágenes, instalar plugins y temas)
RUN chown -R www-data:www-data /var/www/html

EXPOSE 80