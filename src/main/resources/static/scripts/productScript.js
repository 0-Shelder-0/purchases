async function addProduct(event) {
    const description = event.currentTarget.value;

    const productModel = { description };
    const origin = window.location.origin;

    await fetch(`${origin}/addProduct`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json;charset=utf-8'
        },
        body: JSON.stringify(productModel),
    });

    document.location.reload();
}

async function updateProduct(event) {
    const productId = event.currentTarget.id.split('-').slice(-1)[0];
    const description = document.getElementById(`product-${productId}`).value;
    const isCompleted = document.getElementById(`checkbox-${productId}`).checked;

    const productModel = { productId, description, isCompleted };
    const origin = window.location.origin;

    await fetch(`${origin}/updateProduct`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json;charset=utf-8'
        },
        body: JSON.stringify(productModel),
    });

    document.location.reload();
}

async function deleteProduct(event) {
    const productId = event.currentTarget.id.split('-').slice(-1)[0];

    const productModel = { productId };
    const origin = window.location.origin;

    await fetch(`${origin}/deleteProduct`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json;charset=utf-8'
        },
        body: JSON.stringify(productModel),
    });

    document.location.reload();
}