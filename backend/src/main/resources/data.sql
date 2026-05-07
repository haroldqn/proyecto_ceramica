INSERT IGNORE INTO roles (name) VALUES ('CLIENTE');
INSERT IGNORE INTO roles (name) VALUES ('ADMIN');

-- para poder craer otra cuenta xd (a mi no me funciona sin esto xd)
INSERT INTO roles (name) VALUES ('CLIENTE');

-- Ejemplo de producto
INSERT INTO categories (name, description) VALUES ('Animales', 'Ceramica artesanal');
INSERT INTO size (name, dimension) VALUES ('Small', '3x3 cm');

INSERT INTO products (name, image_url, price, stock, status, id_category, id_size)
VALUES ('Loro azul', '/categorias/loro.png', 85.50, 12, true, 1, 1);