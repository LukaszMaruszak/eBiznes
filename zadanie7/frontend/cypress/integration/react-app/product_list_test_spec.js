describe('Product List', function() {
    beforeEach(() => {
        cy.visit('http://localhost:3000')
    })

    it('Should be visible', function() {
        cy.get('.products-list').should('have.length', 1);
    });

    it('Product should contain icon, name, price, add button', function () {
        cy.get('.products-list__item').first().should('have.length', 1);
        cy.get('.products-list__item-details')
            .first().should('have.length', 1);
        cy.get('.products-list__item-details').first().should('have.length', 1);
        cy.get('.products-list__item-details').first().should('have.length', 1);
        cy.get('.details-price').first().should('have.length', 1);
    })

    it('Should contain 14 elements', function () {
        cy.get('.products-list__item').should('have.length', 14);
    })

    describe('Click Add Button', function() {
        it('Should add element to Shopping Card', function () {
            cy.get('.add-product-button').first().click();

            cy.get('.shopping-cart').click();
            cy.get('.shopping-cart__list').should('have.length', 1);
            cy.get('.shopping-cart__list-item h4').should('have.text', 'Iphone 13 Pro');
            cy.get('.shopping-cart__list-item #quantity-number').should('have.value', 1);
        })
    })

    describe('Double click Add Button', function() {
        it('Should add two element to Shopping Card', function () {
            cy.get('.add-product-button').first().click();
            cy.get('.add-product-button').first().click();

            cy.get('.shopping-cart').click();
            cy.get('.shopping-cart__list').should('have.length', 1);
            cy.get('.shopping-cart__list-item h4').should('have.text', 'Iphone 13 Pro');
            cy.get('.shopping-cart__list-item #quantity-number').should('have.value', 2);
        })
    })
})
