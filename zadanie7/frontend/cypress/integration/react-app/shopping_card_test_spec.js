describe('Shopping Card', function() {
    beforeEach(() => {
        cy.visit('http://localhost:3000')
        cy.get('.shopping-cart').click();
    })

    it('Should be empty', function() {
        cy.get('.shopping-cart__list').should('have.text', 'Koszyk jest pusty');
    });

    it('Button should be disabled', function() {
        cy.get('.order-button').should('be.disabled')
    });

    describe('Shopping Card', function() {
        beforeEach(() => {
            cy.visit('http://localhost:3000');
            cy.get('.add-product-button').first().click();
            cy.get('.shopping-cart').click();
        })

        it('Should not be empty', function() {
            cy.get('.shopping-cart__list').should('not.have.text', 'Koszyk jest pusty');
        });

        it('Button should not be disabled', function() {
            cy.get('.order-button').should('not.be.disabled')
        });

        it('Price should be correct', function() {
            cy.get('.shopping-cart__order-summary h3').should('have.text', 'Razem: 5200 zł')
        });

        it('Click "Zapłać" button open page with payment form', function () {
            cy.get('.shopping-cart__order-button button').click();

            cy.url().should('include', '/payment')
        })
    })
})
